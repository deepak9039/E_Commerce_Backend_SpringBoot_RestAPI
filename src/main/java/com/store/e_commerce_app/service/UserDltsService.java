package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Address;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDltsService {

    @Autowired
    private UserDltsRepository userDltsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDlts creatUserDetails(UserDlts userDlts){
        userDlts.setRole("ROLE_USER");
        String encodePassword = passwordEncoder.encode(userDlts.getPassword());
        userDlts.setPassword(encodePassword);
        return userDltsRepository.save(userDlts);

    }

    public List<UserDlts> getAllUsers(){
        return userDltsRepository.findAll();
    }

    public UserDlts findByUserName(String userName){
        return userDltsRepository.findByUserName(userName);
    }

    public UserDlts findByUserId(Long userId){
        return userDltsRepository.findByUserId(userId);
    }

    public UserDlts findByUserEmail(String email){
        UserDlts userDlts = userDltsRepository.findByEmail(email);
        return  userDltsRepository.findByEmail(email);
    }

    public UserDlts updateUserDetails(UserDlts userDlts){
        UserDlts existingUser = userDltsRepository.findByUserId(userDlts.getUserId());
        if(existingUser == null){
            throw new RuntimeException("User not found");
        }

        // Merge simple fields
        if (userDlts.getUserName() != null) existingUser.setUserName(userDlts.getUserName());
        if (userDlts.getEmail() != null) existingUser.setEmail(userDlts.getEmail());
        if (userDlts.getFirstName() != null) existingUser.setFirstName(userDlts.getFirstName());
        if (userDlts.getLastName() != null) existingUser.setLastName(userDlts.getLastName());
        if (userDlts.getProfilePicture() != null) existingUser.setProfilePicture(userDlts.getProfilePicture());
        if (userDlts.getRole() != null) existingUser.setRole(userDlts.getRole());

        // Handle password: if provided and not blank, encode and set
        if (userDlts.getPassword() != null && !userDlts.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(userDlts.getPassword());
            existingUser.setPassword(encoded);
        }

        // Merge addresses: reuse the managed collection instance to avoid orphanRemoval errors
        List<Address> incomingAddresses = userDlts.getAddresses();
        if (incomingAddresses != null) {
            List<Address> existingAddresses = existingUser.getAddresses();
            if (existingAddresses == null) {
                existingAddresses = new ArrayList<>();
                existingUser.setAddresses(existingAddresses);
            }
            // Clear existing collection (this will mark orphans for removal because of orphanRemoval=true)
            existingAddresses.clear();
            for (Address addr : incomingAddresses) {
                // Ensure the relationship is set to the managed existingUser
                addr.setUserDlts(existingUser);
                existingAddresses.add(addr);
            }
        }

        return userDltsRepository.save(existingUser);
    }

}
