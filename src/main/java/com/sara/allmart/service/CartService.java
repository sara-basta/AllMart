package com.sara.allmart.service;

import com.sara.allmart.entity.Cart;
import com.sara.allmart.entity.CartItem;
import com.sara.allmart.entity.Product;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.repository.CartRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public Cart getCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            // Recalculate total just in case prices changed
            cart.calculateTotal();

            return cart;
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            Cart newCart = new Cart();
            newCart.setUser(user);

            return cartRepository.save(newCart);
        }
    }

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCart(userId);
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
            newItem.setPrice(product.getPrice());

            cart.getItems().add(newItem);
        }

        cart.calculateTotal();
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getCart(userId);

        boolean removed = cart.getItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        if(removed){
            cart.calculateTotal();
            cartRepository.save(cart);
        }
        else {
            throw new ResourceNotFoundException("No such item found in cart!");
        }
        return cart;
    }

    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public void checkout(Long userId) {
        Cart cart = getCart(userId);
        if(!cart.getItems().isEmpty()){
            orderService.createOrderFromCart(userId,cart.getItems());
        }
        clearCart(userId);
    }

}
