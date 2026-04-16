package com.store.e_commerce_app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recent_view", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_dlts_user_id", "product_product_id"})
})
public class RecentView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dlts_user_id")
    private UserDlts userDlts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_product_id")
    private Product product;

    private LocalDateTime viewedAt;

    public RecentView() {}

    public RecentView(UserDlts userDlts, Product product, LocalDateTime viewedAt) {
        this.userDlts = userDlts;
        this.product = product;
        this.viewedAt = viewedAt;
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

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }
}

