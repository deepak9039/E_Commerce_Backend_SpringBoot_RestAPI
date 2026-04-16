package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ProductInvoiceService {

    @Autowired
    ProductOrderRepository productOrderRepository;

    public ProductOrder getProductByUserIdAndOrderId(Long userId, String orderId){
        return productOrderRepository.findByUserDltsUserIdAndOrderId(userId,orderId);
    }

    /**
     * Generate a simple invoice PDF for the given ProductOrder and return as byte[]
     */
    public byte[] generateInvoicePdf(ProductOrder order) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, 800);
                cs.showText("Invoice");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(50, 770);
                cs.showText("Order ID: " + order.getOrderId());
                cs.endText();

                if (order.getOrderDate() != null) {
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, 750);
                    cs.showText("Order Date: " + order.getOrderDate().toString());
                    cs.endText();
                }

                if (order.getUserDlts() != null) {
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, 730);
                    cs.showText("Customer: " + order.getUserDlts().getFirstName() + " " + order.getUserDlts().getLastName());
                    cs.endText();
                }

                // Product details
                if (order.getProduct() != null) {
                    int y = 700;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    cs.newLineAtOffset(50, y);
                    cs.showText("Product Details");
                    cs.endText();

                    y -= 20;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText("Product: " + order.getProduct().getProductName());
                    cs.endText();

                    y -= 18;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText("Quantity: " + order.getQuantity());
                    cs.endText();

                    y -= 18;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText("Unit Price: " + order.getPrice());
                    cs.endText();

                    y -= 18;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    cs.newLineAtOffset(50, y);
                    double total = order.getPrice() * (order.getQuantity() != null ? order.getQuantity() : 0);
                    cs.showText("Total: " + total);
                    cs.endText();
                }

                // Address
                if (order.getOrderAddress() != null) {
                    int y = 520;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    cs.newLineAtOffset(50, y);
                    cs.showText("Shipping Address");
                    cs.endText();

                    y -= 20;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText(order.getOrderAddress().getFirstName() + " " + order.getOrderAddress().getLastName());
                    cs.endText();

                    y -= 18;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText(order.getOrderAddress().getAddress());
                    cs.endText();

                    y -= 18;
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, y);
                    cs.showText(order.getOrderAddress().getCity() + ", " + order.getOrderAddress().getState() + " - " + order.getOrderAddress().getPinCode());
                    cs.endText();
                }

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();

        } catch (IOException ex) {
            throw new RuntimeException("Failed to generate invoice PDF", ex);
        }
    }

}
