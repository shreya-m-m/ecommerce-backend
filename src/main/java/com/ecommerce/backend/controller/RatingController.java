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
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.RatingRequest;
import com.ecommerce.backend.service.RatingService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RatingService ratingService;
	
	@PostMapping("/create")
	public ResponseEntity<Ratings>createRating(@RequestBody RatingRequest req,@RequestHeader("Authorization")String jwt) throws UserException, ProductException, RatingException{
		User user = userService.findUserProfileByJwtId(jwt);
	
		Ratings rating = ratingService.createRating(req, user);
		
		return new  ResponseEntity<Ratings>(rating,HttpStatus.CREATED);
		
	}
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Ratings>>getProductRating(@PathVariable Long productId,@RequestHeader("Authorization")String jwt) throws ProductException, UserException{
		User user = userService.findUserProfileByJwtId(jwt);
		List<Ratings> ratings = ratingService.getProductRatings(productId);
		
		return new  ResponseEntity<>(ratings,HttpStatus.ACCEPTED);
		
	}

}
