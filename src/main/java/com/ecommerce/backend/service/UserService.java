package com.ecommerce.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;


public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwtId(String jwt) throws UserException;
	
	public User updateUser(Long userId, User req)throws UserException;

	public List<User> findAllUsers();
	

}
