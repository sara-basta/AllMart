package com.sara.allmart.controller;

import com.sara.allmart.dto.request.ReviewRequest;
import com.sara.allmart.dto.response.ReviewResponse;
import com.sara.allmart.service.ReviewService;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{productId}")
    public ResponseEntity<ReviewResponse> addReview(@AuthenticationPrincipal UserDetails user, @PathVariable Long productId,@RequestBody ReviewRequest request){
        ReviewResponse response = reviewService.addReview(user.getUsername(), productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(@PathVariable Long productId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> response = reviewService.getProductReviews(productId, page, size);
        return ResponseEntity.ok(response);
    }
}
