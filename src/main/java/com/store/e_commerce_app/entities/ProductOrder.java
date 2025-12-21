package com.store.e_commerce_app.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ProductOrder")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private Date orderDate;

    @ManyToOne
    private Product product;

    private Double price;

    private Integer quantity;

    @ManyToOne
    private UserDlts userDlts;

    private String status;

    private String paymentMethod;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderAddress orderAddress;
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UserDlts getUserDlts() {
        return userDlts;
    }

    public void setUserDlts(UserDlts userDlts) {
        this.userDlts = userDlts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderAddress getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddress orderAddress) {
        this.orderAddress = orderAddress;
    }

    // Default Constructor
    public ProductOrder() {

    }

    public ProductOrder(Long id, String orderId, Date orderDate, Product product, Double price, Integer quantity, UserDlts userDlts, String status, String paymentMethod, OrderAddress orderAddress) {
        this.id = id;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.userDlts = userDlts;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.orderAddress = orderAddress;
    }

}
