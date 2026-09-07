package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.PaymentRequest;
import com.store.e_commerce_app.entities.Payment;
import com.store.e_commerce_app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/process-payment")
    public Payment processPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.paymentService(paymentRequest);
    }


}
