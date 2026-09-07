package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.UserDlts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDltsRepository extends JpaRepository<UserDlts, Long> {

    UserDlts findByUserId(Long userId);
    UserDlts findByUserName(String userName);
    public UserDlts findByEmail(String email);
    List<UserDlts> findByRole(String userRole);
}
