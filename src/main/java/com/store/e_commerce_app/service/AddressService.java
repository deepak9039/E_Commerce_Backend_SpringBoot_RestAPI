package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.UserAddressRequest;
import com.store.e_commerce_app.entities.Address;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.AddressRepository;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    UserDltsRepository userDltsRepository;

    @Autowired
    AddressRepository addressRepository;

    public List<Address> findByUserAddresses(Long userId){
        if(userId == null){
            return null;
        }
        return addressRepository.findByUserDltsUserId(userId);

    }

    public Address saveUserAddress(UserAddressRequest request){

        UserDlts user = userDltsRepository.findByUserId(request.getUserId());
        System.out.println("User" + user);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Address address = new Address();
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setAddress(request.getAddress());
        address.setPinCode(request.getPinCode());
        address.setCountry(request.getCountry());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setUserDlts(user);

        return addressRepository.save(address);
    }

    public Address updateUserAddress(UserAddressRequest request){
        Address existingAddress = addressRepository.findByAddressId(request.getAddressId());
        if(existingAddress == null || !existingAddress.getUserDlts().getUserId().equals(request.getUserId())){
            throw new RuntimeException("Invalid Address Id or User Id");
        }

        existingAddress.setAddressLine(request.getAddressLine());
        existingAddress.setCity(request.getCity());
        existingAddress.setState(request.getState());
        existingAddress.setAddress(request.getAddress());
        existingAddress.setPinCode(request.getPinCode());
        existingAddress.setCountry(request.getCountry());
        existingAddress.setPhoneNumber(request.getPhoneNumber());

        return addressRepository.save(existingAddress);
    }

}
