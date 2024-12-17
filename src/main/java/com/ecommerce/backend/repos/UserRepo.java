package com.ecommerce.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);
	

}
