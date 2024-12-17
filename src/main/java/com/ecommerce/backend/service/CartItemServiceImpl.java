package com.ecommerce.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.CartItemException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.CartItemRepo;
import com.ecommerce.backend.repos.CartRepo;

@Service
public class CartItemServiceImpl implements CartItemService{

	@Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartRepo cartRepo;
	
	@Override
	public CartItem createCartItem(CartItem cartItem) {
	
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
		
		CartItem createCartItem = cartItemRepo.save(cartItem);
		return createCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(userId);
		
		if(user.getUser_id().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity()*item.getProduct().getPrice());
			item.setDiscountedPrice(item.getProduct().getDiscountedPrice()*item.getQuantity());
		}
		return cartItemRepo.save(item);
	}

	@Override
	public CartItem isCartIteExist(Cart cart, Product proudct, String size, Long userId) {
		
		CartItem cartItem = cartItemRepo.isCartItemExist(cart, proudct, size, userId);
		
		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		CartItem cartItem= findCartItemById(cartItemId);
		
		User user = userService.findUserById(cartItem.getUserId());
		
		User reqUser = userService.findUserById(userId);
		
		if(user.getUser_id().equals(reqUser.getUser_id())) {
			cartItemRepo.deleteById(cartItemId);
		}
		else {
			throw new UserException("You Cann't Remove Other User item");
		}
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt =cartItemRepo.findById(cartItemId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("CartItem Not Fount with id:"+cartItemId);
		
	}

}
