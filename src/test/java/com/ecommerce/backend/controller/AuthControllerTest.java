package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.repos.UserRepo;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.UserServiceImplement;
import com.ecommerce.backend.service.WishlistService;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserServiceImplement userServiceImpl;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartService cartService;

    @Mock
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstname("Test");
        user.setLastname("User1");
        user.setRole("ROLE_USER");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setUser_id(1L);
            return savedUser;
        });
        when(cartService.createCart(any(User.class))).thenReturn(new Cart());
        when(wishlistService.createWishlist(any(User.class))).thenReturn(new Wishlist());
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("testToken");

        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        System.out.println("Expected Status: " + HttpStatus.CREATED);
        System.out.println("Actual Status: " + response.getStatusCode());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        System.out.println("Expected Msg: Signup successful");
        System.out.println("Actual Msg: " + response.getBody().getMsg());
        assertNotNull(response.getBody());
        assertEquals("Signup successful", response.getBody().getMsg());

        System.out.println("Expected Token: testToken");
        System.out.println("Actual Token: " + response.getBody().getToken());
        assertEquals("testToken", response.getBody().getToken());
    }

    @Test
    void testSignupEmailAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password@123");
        user.setFirstname("Test");
        user.setLastname("User2");
        user.setRole("ROLE_USER");

        when(userRepo.findByEmail("test@example.com")).thenReturn(user);

        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        System.out.println("Expected Status: " + HttpStatus.BAD_REQUEST);
        System.out.println("Actual Status: " + response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        System.out.println("Expected Msg: Email already exists. Please use a different email.");
        System.out.println("Actual Msg: " + response.getBody().getMsg());
        assertNotNull(response.getBody());
        assertEquals("Email already exists. Please use a different email.", response.getBody().getMsg());
    }

    @Test
    void testSigninSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(userServiceImpl.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("testToken");

        ResponseEntity<AuthResponse> response = authController.loginUserHandler(loginRequest);

        System.out.println("Expected Status: " + HttpStatus.OK);
        System.out.println("Actual Status: " + response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        System.out.println("Expected Msg: Signin successfully");
        System.out.println("Actual Msg: " + response.getBody().getMsg());
        assertNotNull(response.getBody());
        assertEquals("Signin successfully", response.getBody().getMsg());

        System.out.println("Expected Token: testToken");
        System.out.println("Actual Token: " + response.getBody().getToken());
        assertEquals("testToken", response.getBody().getToken());
    }

    @Test
    void testSigninInvalidUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid@example.com");
        loginRequest.setPassword("password");

        when(userServiceImpl.loadUserByUsername(loginRequest.getEmail())).thenReturn(null);

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authController.loginUserHandler(loginRequest);
        });

        System.out.println("Expected Exception Message: Invalid Username...");
        System.out.println("Actual Exception Message: " + exception.getMessage());
        assertEquals("Invalid Username...", exception.getMessage());
    }

    @Test
    void testSigninInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(userServiceImpl.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(false);

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authController.loginUserHandler(loginRequest);
        });

        System.out.println("Expected Exception Message: Invalid Password...");
        System.out.println("Actual Exception Message: " + exception.getMessage());
        assertEquals("Invalid Password...", exception.getMessage());
    }
}