package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.AddToCartRequest;
import com.store.e_commerce_app.dto.UpdateCartQuantityRequest;
import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.service.CartService;
import com.store.e_commerce_app.service.UserDltsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("user")
public class HomeController {

    @Autowired
    private UserDltsService userDltsService;

    @Autowired
    private CartService cartService;

    @PostMapping("createUser")
    public String createUser(@RequestBody UserDlts userDlts, HttpSession session){

        UserDlts existingUserDlts = userDltsService.findByUserName(userDlts.getUserName());
        UserDlts existingEmailDetails = userDltsService.findByUserEmail(userDlts.getEmail());
        if(existingUserDlts != null){
            return "Username already exists";
        }
        if (existingEmailDetails != null) {
            return "Email already exists";
        }
        userDltsService.creatUserDetails(userDlts);
        return "User Details Saved Succesfully";
    }

    @GetMapping("findAllUsers")
    public List<UserDlts> getAllUsers(){
        return userDltsService.getAllUsers();
    }

    @PostMapping("getUser")
    public Object getUser(@RequestBody UserDlts userDlts, HttpSession session){
        UserDlts userDetailRes = userDltsService.findByUserId(userDlts.getUserId());
        return userDetailRes;
    }


    @PostMapping("addToCart")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, HttpSession session){

        Cart cart = cartService.saveCart(request.getProductId(), request.getUserId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("cart")
    public ResponseEntity<?> getCartByUser(@RequestBody UserDlts userDlts, HttpSession session){

        List<Cart> carts = cartService.getCartByUser(userDlts.getUserId());
        Double totalOrderPrice = 0.0;
        List<Cart> updatedCart = new ArrayList<>();
        for (Cart cart : carts) {
            Double totalPrice = (cart.getProduct().getProductPrice() * cart.getQuantity());
            cart.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            cart.setTotalOrderPrice(totalOrderPrice);
            updatedCart.add(cart);
        };

        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("cartCount")
    public ResponseEntity<?> getCartCountByUser(@RequestBody UserDlts userDlts, HttpSession session){
        Integer cartCount = cartService.getCartCountByUser(userDlts.getUserId());
        return ResponseEntity.ok(cartCount);
    }

    @PostMapping("updateCartQuantityPlus")
    public ResponseEntity<?> updateCartQuantityPlus(@RequestBody UpdateCartQuantityRequest request, HttpSession session){
        List<Cart> existingCart = cartService.getCartByUser(request.getId());
        if (existingCart == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        Cart updatedCart = cartService.updateQuantityInc(request);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("updateCartQuantityMinus")
    public ResponseEntity<?> updateCartQuantityMinus(@RequestBody UpdateCartQuantityRequest request, HttpSession session) {
        List<Cart> existingCart = cartService.getCartByUser(request.getId());
        if (existingCart == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        Cart updatedCart = cartService.updateQuantityDec(request);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("removeFromCart")
    public ResponseEntity<?> removeFromCart(@RequestBody UpdateCartQuantityRequest request, HttpSession session) {
        cartService.removeFromCart(request);
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "message", "Cart Item removed successfully"));
    }


}
