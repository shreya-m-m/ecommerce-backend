package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.User;

public interface OrderService {
	
	public MyOrder createOrder(User user, Address address);
	
	public MyOrder findOrderById(Long orderId) throws OrderException;
	
	public List<MyOrder> userOrderHistory(Long userId);
	
	public MyOrder placeOrder(Long orderId)throws OrderException;
	
	public MyOrder confirmOrder(Long orderId)throws OrderException;
	
	public MyOrder shippedOrder(Long orderId)throws OrderException;
	
	public MyOrder deliveredOrder(Long orderId)throws OrderException;
	
	public MyOrder cancledOrder(Long orderId)throws OrderException;

	public List<MyOrder>getAllOrders();
	
	
	public void deleteOrder(Long orderId) throws OrderException;
}
