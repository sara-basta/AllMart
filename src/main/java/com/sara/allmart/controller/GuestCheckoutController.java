package com.sara.allmart.controller;

import com.sara.allmart.dto.request.GuestCheckoutRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.PaymentMethod;
import com.sara.allmart.service.OrderService;
import com.sara.allmart.service.StripeService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class GuestCheckoutController {

    private final OrderService orderService;
    private final StripeService stripeService;

    public GuestCheckoutController(OrderService orderService, StripeService stripeService) {
        this.orderService = orderService;
        this.stripeService = stripeService;
    }

    @PostMapping("/guest")
    public ResponseEntity<Map<String, String>> createGuestCheckout(@Valid @RequestBody GuestCheckoutRequest request) {

        OrderResponse orderResponse = orderService.createGuestOrder(request);

        Map<String, String> response = new HashMap<>();

        if (request.paymentMethod() == PaymentMethod.CREDIT_CARD) {
            try {
                String stripeUrl = stripeService.createCheckoutSession(orderResponse.totalAmount(), orderResponse.id(), request.email());
                response.put("url", stripeUrl);
                return ResponseEntity.ok(response);
            } catch (StripeException e) {
                response.put("error", "Payment service unavailable: " + e.getMessage());
                return ResponseEntity.internalServerError().body(response);
            }
        }

        response.put("url", "/payment-success?orderId=" + orderResponse.id());
        return ResponseEntity.ok(response);
    }
}