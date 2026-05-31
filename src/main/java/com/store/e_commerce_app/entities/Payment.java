package com.store.e_commerce_app.entities;

import com.store.e_commerce_app.util.PaymentMethod;
import com.store.e_commerce_app.util.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String orderId; // set by order flow after payment if needed

    private Double amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String providerTxnId; // id returned by gateway (simulated)

    private String instrumentDetails; // masked card / upi id (not full sensitive data)

    private LocalDateTime createdAt;

    public Payment() {}

    // getters / setters

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // ... generate getters and setters (omitted for brevity)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getProviderTxnId() { return providerTxnId; }
    public void setProviderTxnId(String providerTxnId) { this.providerTxnId = providerTxnId; }
    public String getInstrumentDetails() { return instrumentDetails; }
    public void setInstrumentDetails(String instrumentDetails) { this.instrumentDetails = instrumentDetails; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


}
