package com.example.reviewservice.mapper;

import com.example.reviewservice.dto.request.ReviewCreateRequest;
import com.example.reviewservice.dto.request.ReviewUpdateRequest;
import com.example.reviewservice.dto.response.ReviewResponse;
import com.example.reviewservice.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewCreateRequest reviewCreateRequest);
    ReviewResponse toReviewResponse(Review review);
    void updateReview(ReviewUpdateRequest reviewUpdateRequest,@MappingTarget Review review);
}
