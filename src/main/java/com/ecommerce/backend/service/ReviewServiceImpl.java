package com.ecommerce.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.ProductRepo;
import com.ecommerce.backend.repos.ReviewRepo;
import com.ecommerce.backend.request.ReviewRequest;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewRepo reviewRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ProductService productService;
	
	
	@Override
	public Reviews createReview(ReviewRequest req, User user) throws ReviewException, ProductException {
		Product product = productService.findProductById(req.getProductId());
		Reviews review = new Reviews();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		return reviewRepo.save(review);
	}

	@Override
	public List<Reviews> getAllReview(Long productId) {
		
		return reviewRepo.getAllProductsReview(productId);
	}

	@Override
	public String deleteReview(Long reviewId) throws ReviewException {
		Reviews review = findReviewById(reviewId);
		reviewRepo.delete(review);
		return "Review Deleted Successfully";
	}

	@Override
	public Reviews findReviewById(Long id) throws ReviewException{
		Optional<Reviews> opt = reviewRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ReviewException("Review not found with id"+id);
	}

}
