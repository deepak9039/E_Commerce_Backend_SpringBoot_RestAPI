package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.CartRepositort;
import com.store.e_commerce_app.repositories.ProductRepository;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    @Autowired
    private CartRepositort cartRepository;

    @Autowired
    private UserDltsRepository userDltsRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart saveCart(Long productId, Long userId) {

        UserDlts user = userDltsRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check if product already exists in cart for this user
        Cart cart = cartRepository.findByUserDltsUserIdAndProductProductId(userId, productId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserDlts(user);
            cart.setProduct(product);
            cart.setQuantity(1);
        } else {
            cart.setQuantity(cart.getQuantity() + 1);
        }

        cart.setTotalPrice(cart.getQuantity() * product.getProductPrice());

        return cartRepository.save(cart);
    }

    public List<Cart> getCartByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserDltsUserId(userId);
        return carts;
    }

    public Integer getCartCountByUser(Long userId) {
        return cartRepository.countByUserDltsUserId(userId);
    }

    public Cart updateQuantityInc(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        int newQuantity = cart.getQuantity() + 1;

        cart.setQuantity(newQuantity);
        cart.setTotalPrice(newQuantity * cart.getProduct().getProductPrice());

        return cartRepository.save(cart);
    }

    public Cart updateQuantityDec(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        int newQuantity = cart.getQuantity() -1;

        cart.setQuantity(newQuantity);
        cart.setTotalPrice(newQuantity * cart.getProduct().getProductPrice());

        return cartRepository.save(cart);
    }


}

