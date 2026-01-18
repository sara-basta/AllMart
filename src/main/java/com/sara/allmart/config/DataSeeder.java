package com.sara.allmart.config;

import com.sara.allmart.entity.*;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    DataSeeder (ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository){
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count()>0){
            System.out.println("Data already loaded");
            return;
        }

        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("admin");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        Product pc = new Product();
        pc.setName("PC");
        pc.setDescription("The best PC is the world.");
        pc.setPrice(new BigDecimal("20000"));
        pc.setStockQuantity(100);
        Product phone = new Product();
        phone.setName("PHONE");
        phone.setDescription("The best PHONE is the world.");
        phone.setPrice(new BigDecimal("10000"));
        phone.setStockQuantity(500);

        productRepository.saveAll(List.of(pc,phone));

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        Address address = new Address("Agdal","Rabat",10100);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(pc);
        orderItem.setQuantity(2);
        orderItem.setPriceAtPurchase(new BigDecimal("20000"));

        order.getItems().add(orderItem);
        order.setShippingAddress(address);
        orderRepository.save(order);


    }
}