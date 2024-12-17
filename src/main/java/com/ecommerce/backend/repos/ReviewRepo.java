package com.ecommerce.backend.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;

@Repository
public interface ReviewRepo extends JpaRepository<Reviews, Long> {
	@Query("SELECT r FROM Reviews r WHERE r.product.product_id = :productId")
	public List<Reviews> getAllProductsReview(@Param("productId") Long productId);

}
