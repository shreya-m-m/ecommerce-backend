package com.ecommerce.backend.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.backend.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE " +
		       "(p.category.name = :category OR :category = '') AND " +
		       "((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) AND " +
		       "(:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +  
		       "ORDER BY " +
		       "CASE WHEN :sort = 'priceAsc' THEN p.discountedPrice END ASC, " +
		       "CASE WHEN :sort = 'priceDesc' THEN p.discountedPrice END DESC")
		public List<Product> filterProducts(@Param("category") String category,
		                                    @Param("minPrice") Integer minPrice,
		                                    @Param("maxPrice") Integer maxPrice,
		                                    @Param("minDiscount") Integer minDiscount,
		                                    @Param("sort") String sort);

}
