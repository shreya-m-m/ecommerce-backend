package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.AddressException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.AddressService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	
	
	@GetMapping("/profile")
	public ResponseEntity<User>getUserProfileHandler(@RequestHeader("Authorization")String jwt) throws UserException{
		User user = userService.findUserProfileByJwtId(jwt);
		
		return new  ResponseEntity<User>(user,HttpStatus.ACCEPTED);
		
	}
	
	@PutMapping("/{userId}/update")
	public ResponseEntity<User> updateUser(@RequestBody User req, @PathVariable Long userId) throws UserException {
//	    Address address = addressService.updateAddress(addressId, addressReq);
		
		User user = userService.updateUser(userId, req);
	    
	    return new ResponseEntity<User>(user, HttpStatus.OK);
	}





}
