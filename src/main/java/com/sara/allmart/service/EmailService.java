package com.sara.allmart.service;

import com.sara.allmart.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOrderConfirmation(Order order) {
        String subject = "AllMart - Order Confirmation #" + order.getId();
        String text = "Hello " + order.getUser().getFirstName() + ",\n\n" +
                "Thank you for your purchase! Your order #" + order.getId() + " has been Confirmed successfully.\n" +
                "Total Amount: " + order.getTotalAmount() + " MAD\n\n" +
                "We are now preparing your items for shipment. We will notify you once they are on the way!\n\n" +
                "Best regards,\nThe AllMart Team";

        sendEmail(order.getUser().getEmail(), subject, text);
    }

    public void sendShippingUpdate(Order order) {
        String subject = "AllMart - Your Order #" + order.getId() + " has Shipped!";
        String text = "Hello " + order.getUser().getFirstName() + ",\n\n" +
                "Great news! Your order #" + order.getId() + " is on its way to you.\n\n" +
                "Best regards,\nThe AllMart Team";

        sendEmail(order.getUser().getEmail(), subject, text);
    }

    public void sendDeliveryConfirmation(Order order) {
        String subject = "AllMart - Your Order #" + order.getId() + " has been Delivered";
        String text = "Hello " + order.getUser().getFirstName() + ",\n\n" +
                "Your order #" + order.getId() + " has been delivered. We hope you love your items!\n\n" +
                "Best regards,\nThe AllMart Team";

        sendEmail(order.getUser().getEmail(), subject, text);
    }

    public void sendCancellationNotice(Order order) {
        String subject = "AllMart - Order #" + order.getId() + " Cancelled";
        String text = "Hello " + order.getUser().getFirstName() + ",\n\n" +
                "Your order #" + order.getId() + " has been cancelled.\n\n" +
                "Best regards,\nThe AllMart Team";

        sendEmail(order.getUser().getEmail(), subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
