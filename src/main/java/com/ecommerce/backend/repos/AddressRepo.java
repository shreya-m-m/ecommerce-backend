package com.ecommerce.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
	

}
