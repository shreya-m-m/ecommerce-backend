package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.AddressService;
import com.ecommerce.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    @Autowired
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserProfileHandler() throws UserException {
        // Given
        String jwt = "Bearer valid-token";
        User expectedUser = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(expectedUser);

        // When
        ResponseEntity<User> response = userController.getUserProfileHandler(jwt);

        // Expected and Actual Output
        System.out.println("Expected: " + expectedUser.getFirstname());
        System.out.println("Actual: " + response.getBody().getFirstname());

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUser.getUser_id(), response.getBody().getUser_id());
        assertEquals(expectedUser.getFirstname(), response.getBody().getFirstname());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
    }

    @Test
    public void testUpdateUser() throws UserException {
        // Given
        Long userId = 1L;
        User request = new User(null, "Jane Doe", "janedoe@example.com", "newpassword", null, null, null, null, null, null, null, null);
        User updatedUser = new User(userId, "Jane Doe", "janedoe@example.com", "newpassword", null, null, null, null, null, null, null, null);

        when(userService.updateUser(userId, request)).thenReturn(updatedUser);

        // When
        ResponseEntity<User> response = userController.updateUser(request, userId);

        // Expected and Actual Output
        System.out.println("testUpdateUser Expected: " + updatedUser.getFirstname());
        System.out.println("testUpdateUser Actual: " + response.getBody().getFirstname());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedUser.getUser_id(), response.getBody().getUser_id());
        assertEquals(updatedUser.getFirstname(), response.getBody().getFirstname());

        verify(userService, times(1)).updateUser(userId, request);
    }
}
