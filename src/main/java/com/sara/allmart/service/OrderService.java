package com.sara.allmart.service;

import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.*;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.OrderMapper;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional // so that if saving fails, stock goes back to normal (doesn't get decremented)
    public OrderResponse createOrder (Long userId, Long productId){
        log.info("Attempting to create order for User ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(product.getStockQuantity()<1){
            log.warn("Order failed: Product ID {} is out of stock", productId);
            throw new RuntimeException("Not in stock!");
        }

        product.setStockQuantity(product.getStockQuantity()-1);
        productRepository.save(product);

        OrderItem item = new OrderItem();
        item.setPriceAtPurchase(product.getPrice());
        item.setProduct(product);
        item.setQuantity(1);

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);

        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
        BigDecimal total = item.getPriceAtPurchase().multiply(quantity);

        order.setTotalAmount(total);

        item.setOrder(order);
        Address address = new Address("Anfa", "Casa", 10000);
        order.setShippingAddress(address);
        order.setUser(user);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(item);
        log.info("Order successfully created");
        return orderMapper.toResponse(orderRepository.save(order));
    }

    public OrderResponse getOrderById(Long id) {
        return orderMapper.toResponse(orderRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Order not found with ID: {}", id);
                    return new ResourceNotFoundException("Order not found!");}));
    }

    public List<OrderResponse> getOrderByUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        
        List<Order> ordersByUser = orderRepository.findOrdersByUser(user);
        
        return ordersByUser.stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found !"));
        order.setStatus(newStatus);
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Transactional
    public void createOrderFromCart(Long userId, List<CartItem> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order.setShippingAddress(new Address("Agdal", "Rabat", 10100));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;

        for(CartItem cartItem : items){
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Product " + product.getName() + " is out of stock!");
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getPrice());

            orderItems.add(orderItem);

            BigDecimal lineTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalOrderAmount = totalOrderAmount.add(lineTotal);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalOrderAmount);

        orderRepository.save(order);
    }
}
