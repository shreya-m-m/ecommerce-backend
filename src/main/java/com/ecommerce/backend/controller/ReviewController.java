package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.RatingRequest;
import com.ecommerce.backend.request.ReviewRequest;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/create")
	public ResponseEntity<Reviews>createReview(@RequestBody ReviewRequest req,@RequestHeader("Authorization")String jwt) throws UserException, ProductException,ReviewException{
		User user = userService.findUserProfileByJwtId(jwt);
	
		Reviews review = reviewService.createReview(req, user);
		
		return new  ResponseEntity<Reviews>(review,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Reviews>>getProductReview(@PathVariable Long productId,@RequestHeader("Authorization")String jwt) throws ProductException, UserException{
		User user = userService.findUserProfileByJwtId(jwt);
		List<Reviews> review = reviewService.getAllReview(productId);
		
		return new  ResponseEntity<>(review,HttpStatus.ACCEPTED);
		
	}
	

}
