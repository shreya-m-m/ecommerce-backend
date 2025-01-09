package com.ecommerce.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishlistService;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private ProductService productService;

    @Mock
    private User user;

    @Mock
    private Cart cart;

    @Mock
    private Product product;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserCart() throws UserException {
        // Given
        String jwt = "valid-jwt-token";
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(cartService.findUserCart(user.getUser_id())).thenReturn(cart);

        // When
        ResponseEntity<Cart> response = cartController.findUserCart(jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + cart);
        System.out.println("Actual Output: " + response.getBody());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testAddItemToCart() throws UserException, ProductException {
        // Given
        String jwt = "valid-jwt-token";
        AddItemRequest request = new AddItemRequest(1L, jwt, 2, null);
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);

        ApiResponse expectedResponse = new ApiResponse("Item Added to Cart Successfully", true);
        ResponseEntity<ApiResponse> expectedResponseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // When
        ResponseEntity<ApiResponse> response = cartController.addItemToCart(request, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + expectedResponseEntity.getBody().getMsg());
        System.out.println("Actual Output: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseEntity.getBody().getMsg(), response.getBody().getMsg());
    }

    @Test
    public void testAddProductToWishlist_Success() throws UserException, ProductException {
        // Given
        String jwt = "valid-jwt-token";
        AddItemRequest request = new AddItemRequest(1L, jwt, 2, null);
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(productService.findProductById(1L)).thenReturn(product);
        when(wishlistService.addWishlistItem(user.getUser_id(), request, product.getProduct_id())).thenReturn("Item Added to Wishlist");

        ApiResponse expectedResponse = new ApiResponse("Item Added to Wishlist Successfully", true);
        ResponseEntity<ApiResponse> expectedResponseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // When
        ResponseEntity<ApiResponse> response = cartController.addProductToWishlist(request, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + expectedResponseEntity.getBody().getMsg());
        System.out.println("Actual Output: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseEntity.getBody().getMsg(), response.getBody().getMsg());
    }

    @Test
    public void testAddProductToWishlist_ItemAlreadyExists() throws UserException, ProductException {
        // Given
        String jwt = "valid-jwt-token";
        AddItemRequest request = new AddItemRequest(1L, jwt, 2, null);
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(productService.findProductById(1L)).thenReturn(product);
        when(wishlistService.addWishlistItem(user.getUser_id(), request, product.getProduct_id())).thenReturn("Item already exists in the wishlist");

        ApiResponse expectedResponse = new ApiResponse("Item already exists in the wishlist", false);
        ResponseEntity<ApiResponse> expectedResponseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.BAD_REQUEST);

        // When
        ResponseEntity<ApiResponse> response = cartController.addProductToWishlist(request, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + expectedResponseEntity.getBody().getMsg());
        System.out.println("Actual Output: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedResponseEntity.getBody().getMsg(), response.getBody().getMsg());
    }
}
