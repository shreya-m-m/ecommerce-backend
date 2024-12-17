package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.RatingService;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private UserService userService;
	

	@PostMapping("/")
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req) {
		Product product = productService.createProduct(req);

		return new ResponseEntity<Product>(product, HttpStatus.CREATED);

	}

	@DeleteMapping("/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
		productService.deleteProduct(productId);
		ApiResponse res = new ApiResponse();
		res.setMsg("Product Deleted Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@GetMapping("/all")
	public ResponseEntity<List<Product>> findAllProducts() {
		List<Product> product = productService.findAllProducts();
		return new ResponseEntity<>(product, HttpStatus.OK);

	}
	

	@GetMapping("/all-users")
	public ResponseEntity<List<User>> findAllUsers() {
		List<User> users = userService.findAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);

	}
	
	
	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProduct(@RequestBody Product req, @PathVariable Long productId)
			throws ProductException {
		Product product = productService.updateProduct(productId, req);
		return new ResponseEntity<Product>(product, HttpStatus.CREATED);

	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req) {
		for (CreateProductRequest product : req) {
			productService.createProduct(product);
		}
		ApiResponse res = new ApiResponse();
		res.setMsg("Product CreatedSuccessfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.CREATED);

	}
	@DeleteMapping("{reviewId}/review/delete")
	public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) throws ProductException , ReviewException {
		reviewService.deleteReview(reviewId);
		ApiResponse res = new ApiResponse();
		res.setMsg("Review Deleted Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}
	@DeleteMapping("{ratingId}/rating/delete")
	public ResponseEntity<ApiResponse> deleteRating(@PathVariable Long ratingId) throws ProductException , ReviewException, RatingException {
		ratingService.deleteRating(ratingId);
		ApiResponse res = new ApiResponse();
		res.setMsg("Rating Deleted Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

}
