package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.RatingService;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

class AdminProductControllerTest {

    @InjectMocks
    private AdminProductController controller;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private RatingService ratingService;

    public AdminProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    	 public void testCreateProduct() {
    	        CreateProductRequest request = new CreateProductRequest("Dress", "Clothing", 999, 899, 10, 5, 
    	                "BrandX", "Black", new HashSet<>(), "image_url", "Clothing", "Dress", "Women");
    	        Product expectedProduct = new Product(1L, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
    	                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
    	                LocalDateTime.now());

    	        when(productService.createProduct(request)).thenReturn(expectedProduct);

    	        ResponseEntity<Product> response = controller.createProduct(request);

    	        assertEquals(201, response.getStatusCodeValue());
    	        assertEquals(expectedProduct, response.getBody());
    	    }

    @Test
    public void testDeleteProduct() throws ProductException {
        ApiResponse expectedResponse = new ApiResponse("Product Deleted Successfully", true);

        // Mock non-void method using when().thenReturn()
        when(productService.deleteProduct(1L)).thenReturn(expectedResponse.getMsg());

        // Call the controller method
        ResponseEntity<ApiResponse> response = controller.deleteProduct(1L);

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedResponse);
        System.out.println("Actual Output: " + response.getBody());

        // Assert the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse.getMsg(), response.getBody().getMsg());
    }


    @Test
    public void testFindAllProducts() {
        List<Product> expectedProducts = Arrays.asList(
                new Product(1L, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
    	                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
    	                LocalDateTime.now()),
                new Product(1L, "Watch", "Accessories", 999, 899, 10, 5, "Fastrack", "Black", 
    	                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
    	                LocalDateTime.now())
        );

        when(productService.findAllProducts()).thenReturn(expectedProducts);

        ResponseEntity<List<Product>> response = controller.findAllProducts();

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedProducts.toString());
        System.out.println("Actual Output: " + response.getBody().toString());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void testFindAllUsers() {
        List<User> expectedUsers = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", null, null, null, null, null, null, null, null, null),
                new User(2L, "Jane Doe", "jane@example.com", null, null, null, null, null, null, null, null, null)
        );

        when(userService.findAllUsers()).thenReturn(expectedUsers);

        ResponseEntity<List<User>> response = controller.findAllUsers();

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedUsers.toString());
        System.out.println("Actual Output: " + response.getBody().toString());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    public void testUpdateProduct() throws ProductException {
        Product request = new Product(null, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());
        Product expectedProduct = new Product(1L, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());

        when(productService.updateProduct(1L, request)).thenReturn(expectedProduct);

        ResponseEntity<Product> response = controller.updateProduct(request, 1L);

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedProduct);
        System.out.println("Actual Output: " + response.getBody());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(expectedProduct, response.getBody());
    }

    @Test
    public void testCreateMultipleProduct() {
        // Create an array of CreateProductRequest objects
        CreateProductRequest[] requests = {
                new CreateProductRequest("tops", "Clothing", 999, 899, 10, 5, 
                        "BrandX", "Black", new HashSet<>(), "image_url", "Clothing", "Dress", "Women"),
                new CreateProductRequest("jeans", "Clothing", 999, 899, 10, 5, 
                        "BrandX", "Black", new HashSet<>(), "image_url", "Clothing", "Dress", "Women")
        };

        // Expected Product response after creation (make sure it's of the correct type)
        Product expectedProduct = new Product();
        
        // Mock the createProduct method to return the Product object for any CreateProductRequest
        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(expectedProduct);  // Return the Product object

        // Call the controller method
        ResponseEntity<ApiResponse> response = controller.createMultipleProduct(requests);

        // Print expected and actual output for debugging
        System.out.println("Expected Output: " + expectedProduct);
        System.out.println("Actual Output: " + response.getBody());

        // Expected response message
        ApiResponse expectedResponse = new ApiResponse("Product Created Successfully", true);

        // Assert the response status code and message
        assertEquals(201, response.getStatusCodeValue());  // Check if status is 201 (Created)
        assertEquals(expectedResponse.getMsg(), response.getBody().getMsg());  // Verify the message
    }


//
    @Test
    public void testDeleteReview() throws ReviewException, ProductException {
        ApiResponse expectedResponse = new ApiResponse("Review Deleted Successfully", true);

       when(reviewService.deleteReview(1L)).thenReturn(expectedResponse.getMsg());

        ResponseEntity<ApiResponse> response = controller.deleteReview(1L);

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedResponse);
        System.out.println("Actual Output: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse.getMsg(), response.getBody().getMsg());
    }

    @Test
    public void testDeleteRating() throws RatingException, ProductException, ReviewException {
        ApiResponse expectedResponse = new ApiResponse("Rating Deleted Successfully", true);

        when(ratingService.deleteRating(1L)).thenReturn(expectedResponse.getMsg());

        ResponseEntity<ApiResponse> response = controller.deleteRating(1L);

        // Print expected and actual output
        System.out.println("Expected Output: " + expectedResponse);
        System.out.println("Actual Output: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse.getMsg(), response.getBody().getMsg());
    }
}
