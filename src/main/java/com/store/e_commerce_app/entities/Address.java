package com.store.e_commerce_app.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Long addressId;

    @Column(name = "addressLine")
    private String addressLine;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserDlts userDlts;

    // Getters & Setters

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserDlts getUserDlts() {
        return userDlts;
    }

    public void setUserDlts(UserDlts userDlts) {
        this.userDlts = userDlts;
    }

    // Default Constractor
    public Address() {
    }

    public Address(UserDlts userDlts){
        this.userDlts = userDlts;
    }

    public Address(Long addressId, String addressLine, String city, String state, String country, String phoneNumber, UserDlts userDlts) {
        this.addressId = addressId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.userDlts = userDlts;
    }
}
