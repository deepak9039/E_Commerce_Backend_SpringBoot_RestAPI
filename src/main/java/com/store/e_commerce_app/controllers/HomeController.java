package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.AddToCartRequest;
import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.service.CartService;
import com.store.e_commerce_app.service.UserDltsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(carts);
    }

    @PostMapping("cartCount")
    public ResponseEntity<?> getCartCountByUser(@RequestBody UserDlts userDlts, HttpSession session){
        Integer cartCount = cartService.getCartCountByUser(userDlts.getUserId());
        return ResponseEntity.ok(cartCount);
    }

}
