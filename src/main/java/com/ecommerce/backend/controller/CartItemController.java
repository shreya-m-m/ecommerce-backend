package com.ecommerce.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.CartItemException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CartItemService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/cart_item")
public class CartItemController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,@RequestHeader("Authorization")String jwt) throws UserException, CartItemException{
		User user = userService.findUserProfileByJwtId(jwt);
		cartItemService.removeCartItem(user.getUser_id(), cartItemId);
		ApiResponse res = new ApiResponse();
		res.setMsg("Item Removed From Cart" );
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);

	
	}
	@PutMapping("/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,@PathVariable Long cartItemId,@RequestHeader("Authorization")String jwt) throws UserException, CartItemException{
		User user = userService.findUserProfileByJwtId(jwt);
		
		CartItem updateCartItem = cartItemService.updateCartItem(user.getUser_id(), cartItemId, cartItem);
		return new ResponseEntity<>(updateCartItem,HttpStatus.OK);

	}
}
