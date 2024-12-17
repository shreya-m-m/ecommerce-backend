package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.AddressException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Product;

public interface AddressService {
	public Address updateAddress(Long addressId, Address req) throws AddressException;
	
	public Address findAddressById(Long id) throws AddressException;
	
	

}
