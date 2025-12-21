package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Address;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public List<Address> findByUserAddresses(Long userId){
        if(userId == null){
            return null;
        }
        return addressRepository.findByUserDltsUserId(userId);

    }
}
