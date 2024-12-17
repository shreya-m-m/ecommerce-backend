package com.ecommerce.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.ReviewRequest;

public interface ReviewService {
	
	public Reviews createReview(ReviewRequest req, User user)throws ProductException, ReviewException;

	public Reviews findReviewById(Long id)throws ReviewException;
	public List<Reviews>getAllReview(Long productId);
	public String deleteReview(Long reviewId)throws ReviewException;
	
}
