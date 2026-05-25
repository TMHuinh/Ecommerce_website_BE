package com.example.reviewservice.service;

import com.example.reviewservice.dto.request.ReviewCreateRequest;
import com.example.reviewservice.dto.request.ReviewUpdateRequest;
import com.example.reviewservice.dto.response.ReviewResponse;
import com.example.reviewservice.entity.Review;
import com.example.reviewservice.enums.ErrorCode;
import com.example.reviewservice.exception.AppException;
import com.example.reviewservice.mapper.ReviewMapper;
import com.example.reviewservice.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    public ReviewResponse createReview(ReviewCreateRequest request){
        Review review = reviewMapper.toReview(request);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }
    public List<ReviewResponse> getAllReviews(){
        return reviewRepository.findAll().stream().map(reviewMapper::toReviewResponse).toList();
    }
    public List<ReviewResponse> getReviewsByProductId(String productId){
        List<Review> reviews = reviewRepository.findAllByProductID(productId);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }
    public List<ReviewResponse> getReviewsByAccountID(String accountID){
        List<Review> reviews = reviewRepository.findAllByAccountID(accountID);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }
    public ReviewResponse updateReview(String reviewID,ReviewUpdateRequest request){
        Review review = reviewRepository.findById(reviewID).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        reviewMapper.updateReview(request,review);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }
    public void deleteReview(String reviewID){
        reviewRepository.deleteById(reviewID);
    }
}
