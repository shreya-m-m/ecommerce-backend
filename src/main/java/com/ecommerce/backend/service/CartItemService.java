package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.CartItemException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem )throws CartItemException, UserException;
	
	public CartItem isCartIteExist(Cart cart , Product proudct, String size, Long userId);
	
	public void removeCartItem(Long userId, Long cartItemId)throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId)throws CartItemException;

}
