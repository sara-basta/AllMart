package com.sara.allmart.service;

import com.sara.allmart.dto.request.CartRequest;
import com.sara.allmart.dto.response.CartResponse;
import com.sara.allmart.entity.Cart;
import com.sara.allmart.entity.CartItem;
import com.sara.allmart.entity.Product;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.CartMapper;
import com.sara.allmart.repository.CartRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, OrderService orderService, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.cartMapper = cartMapper;
    }

    private Cart getCartEntity(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
    public CartResponse getCart(String email) {
        return cartMapper.toResponse(getCartEntity(email));
    }

    public CartResponse addToCart(String email,Long productId, int quantity) {
        Cart cart = getCartEntity(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        if(product.getStockQuantity()<quantity){
            throw new RuntimeException("Insufficient stock! Only " + product.getStockQuantity() + " left.");
        }

        // check if product is already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(quantity);

            cart.getItems().add(newItem);
        }

        cart.calculateTotal();
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    public CartResponse removeFromCart(String email, Long cartItemId) {
        Cart cart = getCartEntity(email);

        boolean removed = cart.getItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        if(removed){
            cart.calculateTotal();
            cartRepository.save(cart);
        }
        else {
            throw new ResourceNotFoundException("No such item found in cart!");
        }
        return cartMapper.toResponse(cart);
    }

    public void clearCart(String email) {
        Cart cart = getCartEntity(email);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Long checkout(String email,Long addressId) {
        Cart cart = getCartEntity(email);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart.");
        }

        Long orderId = orderService.createOrderFromCart(email, cart.getItems(), addressId);

        clearCart(email);

        return orderId;
    }

    @Transactional
    public CartResponse syncCart(String email, @Valid List<CartRequest> items) {
        // delete old items
        this.clearCart(email);

        // add the new ones from guest cart to user cart
        for (CartRequest item : items) {
            this.addToCart(email, item.productId(), item.quantity());
        }

        return this.getCart(email);
    }
}
