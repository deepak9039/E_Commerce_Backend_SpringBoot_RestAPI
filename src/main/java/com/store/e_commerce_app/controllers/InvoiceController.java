package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.InvoiceDownloadRequestDTO;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.service.ProductInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InvoiceController {

    @Autowired
    private ProductInvoiceService productInvoiceService;

    @PostMapping("/downloadInvoice")
    public ResponseEntity<?> downloadInvoice(@RequestBody InvoiceDownloadRequestDTO request) {

        ProductOrder order = productInvoiceService
                .getProductByUserIdAndOrderId(request.getUserId(), request.getOrderId());

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "FAILED",
                    "message", "No order found for userId: " + request.getUserId()
                            + " and orderId: " + request.getOrderId()
            ));
        }

        byte[] pdfBytes = productInvoiceService.generateInvoicePdf(order);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "invoice_" + order.getOrderId() + ".pdf"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
