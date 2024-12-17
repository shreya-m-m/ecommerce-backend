package com.ecommerce.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.RatingRequest;

public interface RatingService {
	
	public Ratings createRating(RatingRequest req, User user)throws ProductException, RatingException;

	public List<Ratings>getProductRatings(Long productId);
	public Ratings findRatingById(Long id)throws RatingException;
	public String deleteRating(Long ratingId)throws RatingException;
}
