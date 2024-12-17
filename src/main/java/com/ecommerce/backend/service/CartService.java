package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;

public interface CartService {
	public Cart createCart(User user);
	
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException;

	public Cart findUserCart(Long userId);

}
