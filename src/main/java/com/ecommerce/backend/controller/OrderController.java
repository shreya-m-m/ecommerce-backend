package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Address;

import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.OrderItemService;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<MyOrder>createOrder(@RequestBody Address shippingAddress,@RequestHeader("Authorization")String jwt) throws UserException{
		User user = userService.findUserProfileByJwtId(jwt);
	
		MyOrder order = orderService.createOrder(user, shippingAddress);
		System.out.println("MyOrder "+order);
		return new  ResponseEntity<MyOrder>(order,HttpStatus.CREATED);
		
	}
	@GetMapping("/account/order")
	public ResponseEntity<List<MyOrder>>userOrderHistory(@RequestHeader("Authorization")String jwt) throws UserException{
		User user = userService.findUserProfileByJwtId(jwt);
		List<MyOrder> order = orderService.userOrderHistory(user.getUser_id());
		
		return new  ResponseEntity<>(order,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/account/order/{orderId}")
	public ResponseEntity<MyOrder>findOrderById(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws UserException, OrderException{
		User user = userService.findUserProfileByJwtId(jwt);
		MyOrder order = orderService.findOrderById(orderId);
		
		return new  ResponseEntity<MyOrder>(order,HttpStatus.ACCEPTED);
		


	}
	
	@GetMapping("/account/orderItem/{orderItemId}")
	public ResponseEntity<OrderItem>FindByOrderItemId(@PathVariable Long orderItemId,@RequestHeader("Authorization")String jwt) throws UserException, OrderException{
		User user = userService.findUserProfileByJwtId(jwt);
		OrderItem order = orderItemService.findByOrderItemId(orderItemId);
		
		return new  ResponseEntity<OrderItem>(order,HttpStatus.ACCEPTED);
		


	}
}


