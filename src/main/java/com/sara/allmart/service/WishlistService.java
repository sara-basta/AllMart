package com.sara.allmart.service;

import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Product;
import com.sara.allmart.entity.User;
import com.sara.allmart.entity.Wishlist;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.ProductMapper;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.UserRepository;
import com.sara.allmart.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse addToWishlist(String email, Long productId){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        if(wishlistRepository.existsByUserAndProduct_Id(user,productId)){
            throw new RuntimeException("Product already added to wishlist.");
        }
        Wishlist wishlist = new Wishlist(user,product);
        wishlistRepository.save(wishlist);
        return productMapper.toResponse(wishlist.getProduct());
    }

    @Transactional
    public void removeFromWishlist(String email, Long productId){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
       wishlistRepository.deleteByUserAndProduct_Id(user, productId);
    }

    public Page<ProductResponse> getAllWishlist(String email,int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Page<Wishlist> wishlistPage = wishlistRepository.findByUser(user, pageable);
        return wishlistPage.map(w -> productMapper.toResponse(w.getProduct()));
    }
}
