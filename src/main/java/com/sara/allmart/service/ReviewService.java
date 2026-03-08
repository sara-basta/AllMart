package com.sara.allmart.service;

import com.sara.allmart.dto.request.ReviewRequest;
import com.sara.allmart.dto.response.ReviewResponse;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.Product;
import com.sara.allmart.entity.Review;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.ReviewMapper;
import com.sara.allmart.repository.OrderRepository;
import com.sara.allmart.repository.ProductRepository;
import com.sara.allmart.repository.ReviewRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponse addReview(String email, Long productId, ReviewRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        long deliveredCount = orderRepository.countDeliveredProductForUser(user, productId, OrderStatus.DELIVERED);
        long reviewCount = reviewRepository.countByUserAndProduct_Id(user, productId);

        if(deliveredCount == 0){
            throw new IllegalStateException("You can only write a review if you have purchased and received this product.");
        }
        if(reviewCount >= deliveredCount){
            throw new IllegalStateException("You have already reviewed all your delivered orders for this product.");
        }

        int oldCount = product.getReviewCount();
        double oldAverage = product.getAverageRating();

        double newAverage = ((oldAverage * oldCount) + request.rating()) / (oldCount + 1);

        product.setReviewCount(oldCount+1);
        product.setAverageRating(newAverage);
        productRepository.save(product);
        Review review = new Review(user,product,request.rating(), request.comment());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    public Page<ReviewResponse> getProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByProduct_Id(productId, pageable);
        return reviews.map(reviewMapper::toResponse);
    }

    public boolean canReview(String email, Long productId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }

        long deliveredCount = orderRepository.countDeliveredProductForUser(user, productId, OrderStatus.DELIVERED);
        long reviewCount = reviewRepository.countByUserAndProduct_Id(user, productId);

        return deliveredCount > reviewCount;
    }

}
