package com.store.e_commerce_app.entities;

import jakarta.persistence.*;

@Entity
@Table(name="Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserDlts userDlts;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalOrderPrice;

    @Transient
    private Double totalOrderDiscount;

    public Double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(Double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public Double getTotalOrderDiscount() {
        return totalOrderDiscount;
    }

    public void setTotalOrderDiscount(Double totalOrderDiscount) {
        this.totalOrderDiscount = totalOrderDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDlts getUserDlts() {
        return userDlts;
    }

    public void setUserDlts(UserDlts userDlts) {
        this.userDlts = userDlts;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Cart(){

    }

    public Cart(Long id, UserDlts userDlts, Product product, Integer quantity, Double totalPrice, Double totalOrderPrice, Double totalOrderDiscount) {
        this.id = id;
        this.userDlts = userDlts;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.totalOrderPrice = totalOrderPrice;
        this.totalOrderDiscount = totalOrderDiscount;
    }
}
