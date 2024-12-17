package com.ecommerce.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{

}
