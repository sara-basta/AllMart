package com.sara.allmart.service;

import com.sara.allmart.dto.request.AddressRequest;
import com.sara.allmart.dto.request.OrderRequest;
import com.sara.allmart.dto.response.OrderResponse;
import com.sara.allmart.entity.*;
import com.sara.allmart.event.OrderStatusEvent;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.OrderMapper;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.SavedAddressRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final SavedAddressRepository savedAddressRepository;
    private final ApplicationEventPublisher eventPublisher;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper, SavedAddressRepository savedAddressRepository, ApplicationEventPublisher eventPublisher){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.savedAddressRepository = savedAddressRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse createOrder (String email, OrderRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(product.getStockQuantity()<1){
            log.warn("Order failed: Product ID {} is out of stock", request.productId());
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

        Address shippingAddress;

        if (request.addressId() != null) {
            SavedAddress savedAddress = savedAddressRepository.findById(request.addressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            if (!savedAddress.getUser().getEmail().equals(email)) {
                throw new RuntimeException("Access Denied: You cannot use an address that isn't yours!");
            }
            shippingAddress = new Address(savedAddress.getStreet(), savedAddress.getCity(), savedAddress.getZipCode());

        } else if (request.newAddress() != null) {
            SavedAddress savedAddress = new SavedAddress();
            savedAddress.setStreet(request.newAddress().street());
            savedAddress.setCity(request.newAddress().city());
            savedAddress.setZipCode(request.newAddress().zipCode());
            savedAddress.setUser(user);
            savedAddressRepository.save(savedAddress);

            shippingAddress = new Address(request.newAddress().street(), request.newAddress().city(), request.newAddress().zipCode());
        } else {
            throw new RuntimeException("Checkout failed: You must provide either an existing address ID or a new address.");
        }

        order.setShippingAddress(shippingAddress);
        order.setUser(user);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(item);
        log.info("Order successfully created");
        return orderMapper.toResponse(orderRepository.save(order));
    }

    public OrderResponse getCustomerOrderById(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access Denied: You cannot view someone else's order.");
        }

        return orderMapper.toResponse(order);
    }

    public OrderResponse getAdminOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getOrderByUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        
        List<Order> ordersByUser = orderRepository.findOrdersByUser(user);
        
        return ordersByUser.stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found !"));
        if(order.getStatus().equals(OrderStatus.DELIVERED)){
            throw new IllegalStateException("You cannot change a delivered order's status !");
        }
        order.setStatus(newStatus);
        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusEvent(this, order, newStatus.name()));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public Long createOrderFromCart(String email, List<CartItem> items,Long addressId, AddressRequest newAddress) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Address shippingAddress;

        if (addressId != null) {
            SavedAddress savedAddress = savedAddressRepository.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            if (!savedAddress.getUser().getEmail().equals(email)) {
                throw new RuntimeException("Access Denied: You cannot use an address that isn't yours!");
            }
            shippingAddress = new Address(savedAddress.getStreet(), savedAddress.getCity(), savedAddress.getZipCode());

        } else if (newAddress != null) {
            // create and save the new address to the user's profile automatically
            SavedAddress savedAddress = new SavedAddress();
            savedAddress.setStreet(newAddress.street());
            savedAddress.setCity(newAddress.city());
            savedAddress.setZipCode(newAddress.zipCode());
            savedAddress.setUser(user);
            savedAddressRepository.save(savedAddress);

            shippingAddress = new Address(newAddress.street(), newAddress.city(), newAddress.zipCode());
        } else {
            throw new RuntimeException("Checkout failed: You must provide either an existing address ID or a new address.");
        }


        order.setShippingAddress(shippingAddress);

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

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    public Page<OrderResponse> getOrdersHistory(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<Order> ordersPage = orderRepository.findByUser(user, pageable);

        return ordersPage.map(orderMapper::toResponse);
    }

    @Transactional
    public OrderResponse cancelOrder(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to cancel this order");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order has progressed too far and can no longer be cancelled by the user.");
        }

        List<OrderItem> items = order.getItems();

        for(OrderItem item : items){
            Integer quantity = item.getQuantity();
            Long itemId = item.getProduct().getId();

            Product product = productRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            product.setStockQuantity(product.getStockQuantity()+quantity);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusEvent(this, order, "CANCELLED"));

        return orderMapper.toResponse(order);
    }
}
