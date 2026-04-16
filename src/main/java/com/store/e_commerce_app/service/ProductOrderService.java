package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.OrderRequest;
import com.store.e_commerce_app.dto.SalesOverviewDTO;
import com.store.e_commerce_app.dto.TotalRevenueDTO;
import com.store.e_commerce_app.dto.UpdateOrderStatus;
import com.store.e_commerce_app.entities.Cart;
import com.store.e_commerce_app.entities.OrderAddress;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.exception.InvalidOrderStatusException;
import com.store.e_commerce_app.repositories.CartRepositort;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import com.store.e_commerce_app.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderService {

    @Autowired
    ProductOrderRepository productOrderRepository;

    @Autowired
    private CartRepositort cartRepositort;

    @Autowired
    private ProductRepository productRepository; // used to update product stock when order delivered

    // return list of saved orders so controller can include them in response
    public List<ProductOrder> saveOrder(OrderRequest orderRequest) {

        List<Cart> cart = cartRepositort.findByUserDltsUserId(orderRequest.getUserId());
        if(cart == null || cart.isEmpty()) {
            return new ArrayList<>(); // return empty list to indicate nothing saved
        }

        List<ProductOrder> savedOrders = new ArrayList<>();

        for(Cart cart1 : cart) {

            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrderId(UUID.randomUUID().toString());
            productOrder.setOrderDate(new Date());

            productOrder.setProduct(cart1.getProduct());
//            productOrder.setPrice(cart1.getProduct().getProductPrice());
            Double price;
            if (cart1.getProduct().getDiscountPrice() == null) {
//                productOrder.setPrice(cart1.getProduct().getProductPrice());
                price = cart1.getProduct().getProductPrice();
            }
            else {
//                productOrder.setPrice(cart1.getProduct().getDiscountPrice());
                price = cart1.getProduct().getDiscountPrice();
            }
            // Add delivery charge if price < 1000
            if (price < 1000) {
                price = price + 50;
            }
            productOrder.setPrice(price);
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
            ProductOrder saved = productOrderRepository.save(productOrder);
            savedOrders.add(saved);
            // Clear the cart after placing the order
            //cartRepositort.delete(cart1);
        }


        return savedOrders;
    }

    public List<ProductOrder> getOrdersByUserId(OrderRequest orderRequest) {

        if(orderRequest.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return productOrderRepository.findByUserDltsUserId(orderRequest.getUserId());
    }

//    public List<ProductOrder> getAllOrders() {
//        return productOrderRepository.findAll();
//    }
    public Page<ProductOrder> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        return productOrderRepository.findAll(pageable);
    }

    public ProductOrder updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        // Validate status inside service and throw custom exception on invalid
        String status = updateOrderStatus.getStatus();
        Integer quntity = updateOrderStatus.getQuantity();

        System.out.println("Order Quntity: " + quntity);
        if (status == null || status.isBlank()) {
            throw new InvalidOrderStatusException("Order status is required. Allowed: " + String.join(",", getAllowedStatuses()));
        }

        try {
            OrderStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new InvalidOrderStatusException("Invalid order status: " + status + ". Allowed: " + String.join(",", getAllowedStatuses()));
        }

        ProductOrder productOrder = productOrderRepository.findByOrderId(updateOrderStatus.getOrderId());
        String orderId = updateOrderStatus.getOrderId();
        if(productOrder == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        String previousStatus = productOrder.getStatus();

        // If transitioning to DELIVERED from a non-DELIVERED status, decrement stock
        if (!OrderStatus.DELIVERED.name().equals(previousStatus) && OrderStatus.DELIVERED.name().equals(status)) {
            // determine quantity to deduct: prefer existing order quantity, fallback to update payload
            Integer orderedQty = productOrder.getQuantity();
            if (orderedQty == null) orderedQty = quntity != null ? quntity : 0;

            if (orderedQty > 0) {
                if (productOrder.getProduct() == null) {
                    throw new RuntimeException("Associated product not found for order: " + orderId);
                }
                int currentStock = productOrder.getProduct().getStockQuantity();
                if (currentStock < orderedQty) {
                    throw new RuntimeException("Insufficient stock to mark order as DELIVERED. Current stock: " + currentStock + ", ordered: " + orderedQty);
                }
                productOrder.getProduct().setStockQuantity(currentStock - orderedQty);
                // Persist product change
                productRepository.save(productOrder.getProduct());
            }
        }

        productOrder.setStatus(status);
        return productOrderRepository.save(productOrder);
    }

    // helper to return enum names for controller or other callers
    public List<String> getAllowedStatuses() {
        List<String> allowed = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()) {
            allowed.add(s.name());
        }
        return allowed;
    }

    public Page<ProductOrder> getOrdersByUserIdPaged(OrderRequest orderRequest, int pageNumber, int pageSize) {
        if (orderRequest.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productOrderRepository.findByUserDltsUserId(orderRequest.getUserId(), pageable);
    }

    public List<ProductOrder> getLatestOrders() {
        return productOrderRepository.findAllByOrderByIdDesc();
    }

    public TotalRevenueDTO totalRevenue() {
        return productOrderRepository.getTotalRevenue();
    }

    //sales overview by day of week (Monday, Tuesday, etc.) for the last 7 days
    public List<SalesOverviewDTO> getWeeklySalesOverview(){

        List<Object[]> results = productOrderRepository.getWeeklySalesOverview();

        List<SalesOverviewDTO> list = new ArrayList<>();

        for(Object[] row : results){
            String day = (String) row[0];
            Long orders = ((Number) row[1]).longValue();
            Double sales = ((Number) row[2]).doubleValue();

            list.add(new SalesOverviewDTO(day.trim(), orders, sales));
        }

        return list;
    }

}
