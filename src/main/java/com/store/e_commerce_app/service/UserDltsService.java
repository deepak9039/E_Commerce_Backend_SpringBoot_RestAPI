package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Address;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // default: not approved to be admin/seller
        userDlts.setApprovedBySuperAdmin(false);
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

    // DEV helper: create an admin user (role ROLE_ADMIN) - use only in dev/testing
    public UserDlts createAdmin(UserDlts admin) {
        if (admin == null || admin.getEmail() == null || admin.getPassword() == null) {
            throw new IllegalArgumentException("email and password are required");
        }
        UserDlts exists = userDltsRepository.findByEmail(admin.getEmail());
        if (exists != null) {
            // update role if needed
//            exists.setRole("ROLE_ADMIN");
//            System.out.println("email already exists, updating role to ROLE_ADMIN");
//            return userDltsRepository.save(exists);
            throw new RuntimeException("Email already exists");
        }
        admin.setRole("ROLE_ADMIN");
        String encoded = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encoded);
        // default approval false for admin created via this helper
        admin.setApprovedBySuperAdmin(false);
        return userDltsRepository.save(admin);
    }

    // Update last login time for a user
    public void updateLastLoginTime(Long userId) {
        UserDlts u = userDltsRepository.findByUserId(userId);
        if (u != null) {
            u.setLastLoginTime(LocalDateTime.now());
            userDltsRepository.save(u);
        }
    }

    // Set approval flag (to be called by SUPER_ADMIN) - returns updated user
    public UserDlts setApprovedBySuperAdmin(Long userId, boolean approved) {
        UserDlts u = userDltsRepository.findByUserId(userId);
        if (u == null) throw new RuntimeException("User not found");
        u.setApprovedBySuperAdmin(approved);
        return userDltsRepository.save(u);
    }

    // Get All Sellers
    public List<UserDlts> getAllSellers(String role) {
        return userDltsRepository.findByRole(role);
    }


}
