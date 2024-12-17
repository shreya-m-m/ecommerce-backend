package com.ecommerce.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User>user = userRepo.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("User Not Found with ID "+userId);
	}

	@Override
	public User findUserProfileByJwtId(String jwt) throws UserException {
		String email = jwtProvider.getEmailFromToken(jwt);
		User user = userRepo.findByEmail(email);
		
		if(user==null) {
			throw new UserException("User not found with email"+email);
		}
		return user;
	}

	@Override
	public User updateUser(Long userId, User req) throws UserException {
	    // Find the user by ID
	    User user = findUserById(userId);
	    System.out.println("Before save: " + req.getPhone_number());
	    
	    if (user == null) {
	        throw new UserException("User not found with ID: " + userId);
	    }

	    // Check and update the fields
	    if (req.getFirstname() != null && !req.getFirstname().isEmpty()) {
	        user.setFirstname(req.getFirstname());
	    }
	    
	    if (req.getLastname() != null && !req.getLastname().isEmpty()) {
	        user.setLastname(req.getLastname());
	    }
	    
	    if (req.getPassword() != null && !req.getPassword().isEmpty()) {
	        user.setPassword(req.getPassword());
	    }
	    
	    if (req.getEmail() != null && !req.getEmail().isEmpty()) {
	        user.setEmail(req.getEmail());
	    }
	    
	    if (req.getRole() != null && !req.getRole().isEmpty()) {
	        user.setRole(req.getRole());
	    }
	    
	    if (req.getPhone_number() != null && !req.getPhone_number().isEmpty()) {
	        user.setPhone_number(req.getPhone_number());
	        System.out.println("After save: " +req.getPhone_number());
	    }
	    
	    if (req.getAddress() != null && !req.getAddress().isEmpty()) {
	        user.setAddress(req.getAddress());
	        
	        System.out.println("REQ  "+req.getAddress() );
	    }
	    
	    if (req.getPaymentInfo() != null && !req.getPaymentInfo().isEmpty()) {
	        user.setPaymentInfo(req.getPaymentInfo());
	    }
	    
	    if (req.getRatings() != null && !req.getRatings().isEmpty()) {
	        user.setRatings(req.getRatings());
	    }
	    
	    if (req.getReviews() != null && !req.getReviews().isEmpty()) {
	        user.setReviews(req.getReviews());
	    }
	   
	 
	    return userRepo.save(user);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}


}
