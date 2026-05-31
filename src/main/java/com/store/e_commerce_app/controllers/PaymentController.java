package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/process-payment")
    public String processPayment() {
        return paymentService.PaymentService();
    }


}
