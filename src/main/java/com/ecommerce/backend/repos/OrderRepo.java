package com.ecommerce.backend.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.MyOrder;

@Repository
public interface OrderRepo extends JpaRepository<MyOrder, Long> {

	@Query("SELECT m FROM MyOrder m WHERE m.user.user_id = :userId AND " +
		       "m.orderStatus IN ('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED')")
		public List<MyOrder> getUserOrders(@Param("userId") Long userId);

}
