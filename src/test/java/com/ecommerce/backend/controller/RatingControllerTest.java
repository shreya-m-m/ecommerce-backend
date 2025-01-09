package com.ecommerce.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.RatingRequest;
import com.ecommerce.backend.service.RatingService;
import com.ecommerce.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class RatingControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private RatingService ratingService;

    @Autowired
    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRating() throws UserException, ProductException, RatingException {
        // Given
    	Long productId = 1L;
        String jwt = "Bearer valid-token";
        RatingRequest request = new RatingRequest(1L, 5); // Adjusted constructor usage
        User user = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
        Ratings expectedRating = new Ratings(1L, user, new Product(), 5.0, LocalDateTime.now());


        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(ratingService.createRating(request, user)).thenReturn(expectedRating);

        // When
        ResponseEntity<Ratings> response = ratingController.createRating(request, jwt);

        // Expected and Actual Output
        System.out.println("Expected: " + expectedRating.getRating());
        System.out.println("Actual: " + response.getBody().getRating());

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedRating.getRating_id(), response.getBody().getRating_id()); // Corrected getter name
        assertEquals(expectedRating.getRating(), response.getBody().getRating()); // Added check for rating value
       

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(ratingService, times(1)).createRating(request, user);
    }

//
    @Test
    public void testGetProductRating() throws ProductException, UserException {
        // Given
        Long productId = 1L;
        String jwt = "Bearer valid-token";
        User user = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
        Ratings rating1 = new Ratings(1L, user, new Product(), 5.0, LocalDateTime.now());
        Ratings rating2 = new Ratings(1L, user, new Product(), 4.5, LocalDateTime.now());
        List<Ratings> expectedRatings = Arrays.asList(rating1, rating2);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(ratingService.getProductRatings(productId)).thenReturn(expectedRatings);

        // When
        ResponseEntity<List<Ratings>> response = ratingController.getProductRating(productId, jwt);

        // Expected and Actual Output
        System.out.println("Expected: " + expectedRatings);
        System.out.println("Actual: " + response.getBody().toArray());

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
//        assertEquals("Excellent product!", response.getBody().get(0).getReview());
//        assertEquals("Very good!", response.getBody().get(1).getReview());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(ratingService, times(1)).getProductRatings(productId);
    }
}
