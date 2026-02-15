package com.sara.allmart.service;

import com.sara.allmart.dto.request.PaymentRequest;
import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.PaymentMethod;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String processPayment(String email, PaymentRequest request) {
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

        if(request.paymentMethod()== PaymentMethod.CREDIT_CARD){
            if(request.cardNumber()==null || request.cardNumber().isBlank()){
                throw new IllegalStateException("Credit card number invalid.");
            }
            order.setPaid(true);
            order.setStatus(OrderStatus.PAID);
        }
        else if (request.paymentMethod()== PaymentMethod.CASH_ON_DELIVERY){
            order.setPaid(false);
            order.setStatus(OrderStatus.CONFIRMED);
        }
        orderRepository.save(order);
        return "Order successfully paid!";
    }
}
