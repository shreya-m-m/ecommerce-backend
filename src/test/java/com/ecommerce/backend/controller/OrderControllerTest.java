package com.ecommerce.backend.controller;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.OrderItemService;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.UserService;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @Mock
    private MyOrder order;

    @Mock
    private Address address;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() throws UserException {
        // Given
        String jwt = "valid-jwt-token";
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(orderService.createOrder(user, address)).thenReturn(order);

        // When
        ResponseEntity<MyOrder> response = orderController.createOrder(address, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + order);
        System.out.println("Actual Output: " + response.getBody());

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    public void testUserOrderHistory() throws UserException {
        // Given
        String jwt = "valid-jwt-token";
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(orderService.userOrderHistory(user.getUser_id())).thenReturn(List.of(order));

        // When
        ResponseEntity<List<MyOrder>> response = orderController.userOrderHistory(jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + List.of(order));
        System.out.println("Actual Output: " + response.getBody());

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(List.of(order), response.getBody());
    }

    @Test
    public void testFindOrderById() throws UserException, OrderException {
        // Given
        Long orderId = 1L;
        String jwt = "valid-jwt-token";
        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(orderService.findOrderById(orderId)).thenReturn(order);

        // When
        ResponseEntity<MyOrder> response = orderController.findOrderById(orderId, jwt);

        // Expected output and actual output
        System.out.println("Expected Output: " + order);
        System.out.println("Actual Output: " + response.getBody());

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(order, response.getBody());
    }
}
