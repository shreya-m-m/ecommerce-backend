package com.ecommerce.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Cart;


@Repository
public interface CartRepo extends JpaRepository<Cart, Long>{

	@Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId")
	public Cart findByUserId(@Param("userId") Long userId);

	

}
