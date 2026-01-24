package com.sara.allmart.config;

import com.sara.allmart.entity.*;
import com.sara.allmart.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    DataSeeder(ProductRepository productRepository, UserRepository userRepository,
               OrderRepository orderRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            System.out.println("Data already loaded");
            return;
        }

        // Create Categories
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Gadgets and devices");
        categoryRepository.save(electronics);

        // Create User
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setFirstName("Sara");
        user.setLastName("Basta");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        // Create Products
        Product pc = new Product();
        pc.setName("PC");
        pc.setDescription("The best PC in the world.");
        pc.setPrice(new BigDecimal("20000"));
        pc.setStockQuantity(100);
        pc.setCategory(electronics);

        Product phone = new Product();
        phone.setName("PHONE");
        phone.setDescription("The best PHONE in the world.");
        phone.setPrice(new BigDecimal("10000"));
        phone.setStockQuantity(500);
        phone.setCategory(electronics);

        productRepository.saveAll(List.of(pc, phone));

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("40000"));

        Address address = new Address("Hay Riad", "Rabat", 10100);
        order.setShippingAddress(address);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(pc);
        orderItem.setQuantity(2);
        orderItem.setPriceAtPurchase(new BigDecimal("20000"));

        order.getItems().add(orderItem);

        orderRepository.save(order);

        System.out.println("Test Data Loaded Successfully!");
    }
}