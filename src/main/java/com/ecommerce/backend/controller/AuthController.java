package com.ecommerce.backend.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.repos.UserRepo;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.UserServiceImplement;
import com.ecommerce.backend.service.WishlistService;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImplement userServiceImpl;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private WishlistService wishlistService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
	    try {
	        // Validate input data (for example, check if fields are empty or invalid)
	        if (user.getEmail() == null || user.getPassword() == null || user.getFirstname() == null || user.getLastname() == null) {
	        	AuthResponse authResponse = new AuthResponse();
	        	authResponse.setMsg("All Feilds Reqiured ");
	            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.BAD_REQUEST);
	        }

	        String email = user.getEmail();
	        String password = user.getPassword();
	        String firstname = user.getFirstname();
	        String lastname = user.getLastname();
	        String role = user.getRole();

	        // Check if email already exists

	        User isEmailExist = userRepo.findByEmail(email);
	        if (isEmailExist != null) {
	        	AuthResponse authResponse = new AuthResponse();
		   		 
		   		 authResponse.setMsg("Email already exists. Please use a different email.");
		   		
	            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.BAD_REQUEST);
	        }

	        // Create the new user
	        User createdUser = new User();
	        createdUser.setEmail(email);
	        createdUser.setPassword(passwordEncoder.encode(password)); // Ensure password is encoded
	        createdUser.setFirstname(firstname);
	        createdUser.setLastname(lastname);
	        createdUser.setRole(role);
	        createdUser.setCreatedAt(LocalDateTime.now());

	        // Save the user to the repository
	        User saveUser = userRepo.save(createdUser);

	        // Create associated cart and wishlist for the new user
	        Cart cart = cartService.createCart(saveUser);
	        Wishlist wishlist = wishlistService.createWishlist(saveUser);

	        // Authenticate the user (Optional, if you need the user to be logged in after registration)
	        Authentication authentication = new UsernamePasswordAuthenticationToken(saveUser.getEmail(), saveUser.getPassword());
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Generate JWT token
	        String token = jwtProvider.generateToken(authentication);

	        // Create response object
	        AuthResponse authResponse = new AuthResponse();
	        authResponse.setToken(token);
	        authResponse.setMsg("Signup successful");

	        // Return response with created status and the token
	        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

	    } catch (Exception e) {
	         AuthResponse authResponse = new AuthResponse();
	        authResponse.setMsg("Signup Failure");

	        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		 authResponse.setToken(token);
		 authResponse.setMsg("Signin successfully");
		
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK); 

		
	}


	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid Username...");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
		    throw new BadCredentialsException("Invalid Password...");
		}

		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
}
