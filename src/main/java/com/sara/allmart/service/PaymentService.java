package com.sara.allmart.service;

import com.sara.allmart.dto.request.PaymentRequest;
import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.PaymentMethod;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.UserRepository;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StripeService stripeService;

    public PaymentService(OrderRepository orderRepository, UserRepository userRepository, StripeService stripeService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.stripeService = stripeService;
    }

    @Transactional
    public Map<String, String> processPayment(String email, PaymentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        if(!user.getId().equals(order.getUser().getId())){
            throw new RuntimeException("This order isn't owned by this user. Access Denied!");
        }
        if(order.isPaid()){
            throw new IllegalStateException("This order had being paid.");
        }
        if(order.getStatus()==OrderStatus.CANCELLED){
            throw new IllegalStateException("This order had been cancelled.");
        }
        order.setPaymentMethod(request.paymentMethod());
        Map<String, String> response = new HashMap<>();

        if(request.paymentMethod() == PaymentMethod.CREDIT_CARD){
            try {
                PaymentIntent intent = stripeService.createPaymentIntent(order.getTotalAmount(), order.getId().toString());
                response.put("clientSecret", intent.getClientSecret());
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Stripe payment: " + e.getMessage());
            }
        }
        else if (request.paymentMethod()== PaymentMethod.CASH_ON_DELIVERY){
            order.setPaid(false);
            order.setStatus(OrderStatus.CONFIRMED);
            response.put("message", "Order successfully placed for Cash on Delivery!");
        }
        orderRepository.save(order);
        return response;
    }
}
