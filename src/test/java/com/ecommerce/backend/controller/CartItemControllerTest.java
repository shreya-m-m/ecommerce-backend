package com.ecommerce.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.CartItemException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CartItemService;
import com.ecommerce.backend.service.UserService;

class CartItemControllerTest {

    @InjectMocks
    private CartItemController cartItemController;

    @Mock
    private UserService userService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private User user;

    @Mock
    private CartItem cartItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteCartItem() throws UserException, CartItemException {
        // Given
        Long cartItemId = 1L;
        String jwt = "valid-jwt-token";
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);

        ApiResponse expectedResponse = new ApiResponse("Item Removed From Cart", true);
        ResponseEntity<ApiResponse> expectedResponseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // When
        ResponseEntity<ApiResponse> response = cartItemController.deleteCartItem(cartItemId, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + expectedResponseEntity.getBody().getMsg());
        System.out.println("Actual Output: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseEntity.getBody().getMsg(), response.getBody().getMsg());
    }

    @Test
    public void testUpdateCartItem() throws UserException, CartItemException {
        // Given
        Long cartItemId = 1L;
        String jwt = "valid-jwt-token";
        CartItem updatedCartItem = new CartItem(); // Assuming CartItem is a POJO with appropriate setters
        updatedCartItem.setCartItems_id(cartItemId);
        updatedCartItem.setQuantity(3); // Example quantity update
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(cartItemService.updateCartItem(user.getUser_id(), cartItemId, updatedCartItem)).thenReturn(updatedCartItem);

        // When
        ResponseEntity<CartItem> response = cartItemController.updateCartItem(updatedCartItem, cartItemId, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + updatedCartItem);
        System.out.println("Actual Output: " + response.getBody());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCartItem, response.getBody());
    }
}
