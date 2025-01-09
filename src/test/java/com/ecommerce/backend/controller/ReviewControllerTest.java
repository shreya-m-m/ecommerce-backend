package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hibernate.resource.transaction.internal.SynchronizationRegistryStandardImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.ReviewRequest;
import com.ecommerce.backend.service.RatingService;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

@SpringBootTest
class ReviewControllerTest {
	@MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;
    
    @Autowired
    @InjectMocks
    private ReviewController reviewController;

	@Test
	void testCreateReview() throws UserException, ProductException, ReviewException {
		String jwt = "Bearer valid-token";
		User user = new User (1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
		ReviewRequest request = new ReviewRequest(1L,"Excellent Product");
		Reviews expectedReview = new Reviews(1L, "Excellent Product", new Product(),user,LocalDateTime.now());
		
		when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
		when(reviewService.createReview(request, user)).thenReturn(expectedReview);
		
		ResponseEntity<Reviews> response = reviewController.createReview(request, jwt);
		
		System.out.println("Expected: "+ expectedReview.getReview());
		System.out.println("Actual: "+response.getBody().getReview());
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(expectedReview.getReview_id(), response.getBody().getReview_id());
		verify(userService, times(1)).findUserProfileByJwtId(jwt);
		verify(reviewService, times(1)).createReview(request, user);
			
		
	}
	
	@Test
	void testGetProductReview() throws UserException, ProductException {
		Long productId = 1L;
		String jwt = "Bearer valid-token";
		User user = new User (1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
		Reviews review1 = new Reviews();
		Reviews review2 = new Reviews();
		
		List<Reviews> expectedReviews = Arrays.asList(review1, review2);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(reviewService.getAllReview(productId)).thenReturn(expectedReviews);
        
    	ResponseEntity<List<Reviews>> response = reviewController.getProductReview(productId, jwt);
    	
    	System.out.println("Expected: "+ expectedReviews);
		System.out.println("Actual: "+response.getBody());
		
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		assertNotNull(response.getBody());
		
		verify(userService, times(1)).findUserProfileByJwtId(jwt);
		verify(reviewService, times(1)).getAllReview(productId);
		
	}

}
