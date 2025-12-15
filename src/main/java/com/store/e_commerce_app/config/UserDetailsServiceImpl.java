package com.store.e_commerce_app.config;

import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDltsRepository userDltsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("LOGIN EMAIL: " + email);

        UserDlts userDlts = userDltsRepository.findByEmail(email);
        if (userDlts == null) {
            System.out.println("USER NOT FOUND");
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        System.out.println("USER FOUND: " + userDlts.getEmail());
        return new CustomUser(userDlts);
    }
}
