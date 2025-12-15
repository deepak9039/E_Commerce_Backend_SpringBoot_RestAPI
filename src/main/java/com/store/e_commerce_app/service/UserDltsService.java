package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}

