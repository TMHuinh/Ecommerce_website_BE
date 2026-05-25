package com.example.reviewservice.controller;

import com.example.reviewservice.dto.request.ReviewCreateRequest;
import com.example.reviewservice.dto.request.ReviewUpdateRequest;
import com.example.reviewservice.dto.response.ApiResponse;
import com.example.reviewservice.dto.response.ReviewResponse;
import com.example.reviewservice.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ReviewController {
    ReviewService reviewService;
    @GetMapping
    public ApiResponse<List<ReviewResponse>> getAllReviews() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getAllReviews())
                .build();
    }
    @PostMapping
    public ApiResponse<ReviewResponse> createReview(@RequestBody ReviewCreateRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request))
                .build();
    }
    @GetMapping("/list-review-of-account/{accountID}")
    public ApiResponse<List<ReviewResponse>> getReviewsByAccountID(@PathVariable String accountID){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByAccountID(accountID))
                .build();
    }
    @GetMapping("/list-review-of-product/{productID}")
    public ApiResponse<List<ReviewResponse>> getReviewsByProductID(@PathVariable String productID){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByProductId(productID))
                .build();
    }
    @PutMapping("/{reviewID}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable String reviewID, @RequestBody ReviewUpdateRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReview(reviewID, request))
                .build();
    }
    @DeleteMapping("/{reviewID}")
    public ApiResponse<?> deleteReview(@PathVariable String reviewID){
        reviewService.deleteReview(reviewID);
        return ApiResponse.builder()
                .message("Review with ID " + reviewID + " was deleted")
                .build();
    }
}
