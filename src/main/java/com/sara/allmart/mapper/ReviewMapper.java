package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.ReviewResponse;
import com.sara.allmart.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review review){
        Long id = review.getId();
        int rating = review.getRating();
        String comment = review.getComment();
        String firstName = review.getUser().getFirstName();

        return new ReviewResponse(id,rating,comment,firstName);
    }
}
