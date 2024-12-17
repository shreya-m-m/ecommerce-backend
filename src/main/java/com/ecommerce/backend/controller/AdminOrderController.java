package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.OrderService;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/")
	public ResponseEntity<List<MyOrder>>getAllOrdersHandler(){
		List<MyOrder> orders= orderService.getAllOrders();
		return new  ResponseEntity<List<MyOrder>>(orders,HttpStatus.ACCEPTED);
		
	}
	
	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<MyOrder> ConfirmedOrdeHandler(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws OrderException{
		MyOrder order = orderService.confirmOrder(orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
		
	}
	@PutMapping("/{orderId}/ship")
	public ResponseEntity<MyOrder> ShippedOrdeHandler(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws OrderException{
		MyOrder order = orderService.shippedOrder(orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
		
	}

	@PutMapping("/{orderId}/deliver")
	public ResponseEntity<MyOrder> DeliverOrdeHandler(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws OrderException{
		MyOrder order = orderService.deliveredOrder(orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
		
	}
	

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<MyOrder> CancelOrdeHandler(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws OrderException{
		MyOrder order = orderService.cancledOrder(orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
		
	}
	

	@DeleteMapping("/{orderId}/delete")
	public ResponseEntity<ApiResponse> DeleteOrdeHandler(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws OrderException{
		orderService.deleteOrder(orderId);
		ApiResponse res = new ApiResponse();
		res.setMsg("MyOrder Deleted Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}


}
