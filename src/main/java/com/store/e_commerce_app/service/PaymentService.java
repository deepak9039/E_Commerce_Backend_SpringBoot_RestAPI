package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.PaymentRequest;
import com.store.e_commerce_app.dto.UserPaymentDetails;
import com.store.e_commerce_app.entities.Payment;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.PaymentRepository;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import com.store.e_commerce_app.util.PaymentMethod;
import com.store.e_commerce_app.util.PaymentStatus;
import com.store.e_commerce_app.util.OrderPaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PaymentService {

//    @Autowired
//    PaymentRepository paymentRepository;
//
//    @Autowired
//    ProductOrderRepository productOrderRepository;

    private final PaymentRepository paymentRepository;
    private final ProductOrderRepository productOrderRepository;

    public PaymentService(PaymentRepository paymentRepository, ProductOrderRepository productOrderRepository) {
        this.paymentRepository = paymentRepository;
        this.productOrderRepository = productOrderRepository;
    }

    @Autowired
    UserDltsRepository userDltsRepository;

    @Transactional
    public Payment paymentService(PaymentRequest request) {

        UserDlts user = userDltsRepository.findByUserId(request.getUserId());
        ProductOrder order = productOrderRepository.findByUserDltsUserIdAndOrderId(request.getUserId(), request.getOrderId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setOrderId(order.getOrderId());
//        if(order.getProduct().getDiscountPrice() == null) {
//            payment.setAmount(order.getProduct().getProductPrice() * order.getQuantity());
//        } else {
//            payment.setAmount(order.getProduct().getDiscountPrice() * order.getQuantity());
//        }
        payment.setAmount(order.getProduct().getDiscountPrice() != null ? order.getProduct().getDiscountPrice() * order.getQuantity() : order.getProduct().getProductPrice() * order.getQuantity());
        payment.setMethod(request.getPaymentMethod());
        payment.setStatus(request.getPaymentStatus());

        // Save payment
        Payment saved = paymentRepository.save(payment);

        // Update order payment status based on saved payment status
        if (saved.getStatus() == null) {
            order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_PENDING);
        } else if (PaymentStatus.SUCCESS.equals(saved.getStatus())) {
            order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_SUCCESSFUL);
        } else if (PaymentStatus.FAILED.equals(saved.getStatus())) {
            order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_FAILED);
        } else {
            order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_PENDING);
        }

        productOrderRepository.save(order);

        return saved;
    }

    public ProductOrder getOrderDetails(UserPaymentDetails request) {
        Long userId = request.getUserId();
        String orderId = request.getOrderId();

        ProductOrder order = productOrderRepository.findByUserDltsUserIdAndOrderId(userId, orderId);
        if(order == null) {
            throw new RuntimeException("Order not found for the given User ID and Order ID");
        }
        return order;
    }

    public Payment getPaymentDetails(UserPaymentDetails request) {
        Long userId = request.getUserId();
        String orderId = request.getOrderId();

        Payment payment = paymentRepository.findByUserUserIdAndOrderId(userId, orderId);
        if(payment == null) {
            throw new RuntimeException("Payment not found for the given User ID and Order ID");
        }
        return payment;
    }

}
