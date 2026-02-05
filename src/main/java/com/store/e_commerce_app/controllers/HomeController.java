package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.*;
import com.store.e_commerce_app.entities.Address;
import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.service.AddressService;
import com.store.e_commerce_app.service.CartService;
import com.store.e_commerce_app.service.ProductOrderService;
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

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private AddressService addressService;

    @PostMapping("createUser")
    public String createUser(@RequestBody UserDlts userDlts, HttpSession session){

//        UserDlts existingUserDlts = userDltsService.findByUserName(userDlts.getUserName());
        UserDlts existingEmailDetails = userDltsService.findByUserEmail(userDlts.getEmail());
//        if(existingUserDlts != null){
//            return "Username already exists";
//        }
        if (existingEmailDetails != null) {
            return "Email already exists";
        }
        userDltsService.creatUserDetails(userDlts);
        return "User Details Saved Succesfully";
    }

    @PostMapping("getUser")
    public Object getUser(@RequestBody UserDlts userDlts, HttpSession session){
        UserDlts userDetailRes = userDltsService.findByUserId(userDlts.getUserId());
        return userDetailRes;
    }

    @PostMapping("updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDlts userDlts, HttpSession session){
        UserDlts updatedUserDetail = userDltsService.updateUserDetails(userDlts);
        if(updatedUserDetail == null){
            return ResponseEntity.ok(Map.of(
                    "status", "FAILURE",
                    "message", "Failed to update user details"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "User details updated successfully",
                "user", updatedUserDetail
        ));
    }

    @PostMapping("createUserAddress")
    public ResponseEntity<?> createUserAddress(@RequestBody UserAddressRequest request){

        Address savedAddress = addressService.saveUserAddress(request);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Address saved successfully",
                "addressId", savedAddress.getAddressId()
        ));

    }

    @PostMapping("updateUserAddress")
    public ResponseEntity<?> updateUserAddress(@RequestBody UserAddressRequest request){
        Address updatedAddress = addressService.updateUserAddress(request);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Address updated successfully",
                "addressId", updatedAddress.getAddressId()
        ));
    }

    @PostMapping("userAddress")
    public ResponseEntity<?> getUserAddress(@RequestBody UserDlts userDlts, HttpSession session){
        List<Address> addresses = addressService.findByUserAddresses(userDlts.getUserId());
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "addresses", addresses
        ));
    }

    @GetMapping("findAllUsers")
    public List<UserDlts> getAllUsers(){
        return userDltsService.getAllUsers();
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

    @PostMapping("saveOrder")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest request, HttpSession session){
        String orderMessage = productOrderService.saveOrder(request);
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "message", orderMessage
        ));

    }

    @PostMapping("getOrdersByUserId")
    public ResponseEntity<?> getOrdersByUserId(@RequestBody OrderRequest request, HttpSession session) {
        // Read pagination from request body (1-based page). Provide defaults when absent.
        int page = (request.getPage() == null || request.getPage() < 1) ? 1 : request.getPage();
        int pageSize = (request.getPageSize() == null || request.getPageSize() < 1) ? 10 : request.getPageSize();

        int pageIndex = page - 1; // convert to 0-based

        var pageResult = productOrderService.getOrdersByUserIdPaged(request, pageIndex, pageSize);

        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "page", page,
                "pageSize", pageSize,
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements(),
                "orders", pageResult.getContent()
        ));
    }

    @PostMapping("/admin/getAllOrders")
    public ResponseEntity<?> getAllOrders(HttpSession session) {
        List<ProductOrder> orders = productOrderService.getAllOrders();
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "orders", orders
        ));
    }

    @PostMapping("/admin/updateOrderStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatus request, HttpSession session){
        try {
            ProductOrder productOrder = productOrderService.updateOrderStatus(request);
            return ResponseEntity.ok(Map.of(
                    "status", "Success",
                    "order", productOrder
            ));
        } catch (IllegalArgumentException ex) {
            // validation error from service
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Failure",
                    "message", ex.getMessage(),
                    "allowedStatuses", productOrderService.getAllowedStatuses()
            ));
        }
    }

    @PostMapping("about")
    public String aboutPage(){
        return "abount page";
    }

}
