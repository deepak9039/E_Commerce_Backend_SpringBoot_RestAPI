package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.OrderRequest;
import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.OrderAddress;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.repositories.CartRepositort;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderService {

    @Autowired
    ProductOrderRepository productOrderRepository;

    @Autowired
    private CartRepositort cartRepositort;

    public String saveOrder(OrderRequest orderRequest) {

        List<Cart> cart = cartRepositort.findByUserDltsUserId(orderRequest.getUserId());
        if(cart == null || cart.isEmpty()) {
            return "Cart is empty for user, cannot place order";
        }

        for(Cart cart1 : cart) {

            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrderId(UUID.randomUUID().toString());
            productOrder.setOrderDate(new Date());

            productOrder.setProduct(cart1.getProduct());
            productOrder.setPrice(cart1.getProduct().getProductPrice());

            productOrder.setQuantity(cart1.getQuantity());
            productOrder.setUserDlts(cart1.getUserDlts());

            productOrder.setStatus(OrderStatus.IN_PROGRESS.name());
            productOrder.setPaymentMethod(orderRequest.getPaymentMethod());

            //Order Addess
            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setPhoneNumber(orderRequest.getPhoneNumber());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPinCode(orderRequest.getPinCode());

            productOrder.setOrderAddress(address);

            System.out.println("Saving order for product: " + cart1.getProduct().getProductName() + " for user ID: " + orderRequest.getUserId());
            System.out.println("Order Details: " + productOrder);
            productOrderRepository.save(productOrder);
            // Clear the cart after placing the order
            //cartRepositort.delete(cart1);
        }



        return "Order saved successfully";
    }

    public List<ProductOrder> getOrdersByUserId(OrderRequest orderRequest) {

        if(orderRequest.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return productOrderRepository.findByUserDltsUserId(orderRequest.getUserId());
    }

}
