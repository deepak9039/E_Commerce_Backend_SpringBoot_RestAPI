package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserDltsUserId(Long userId);

}
