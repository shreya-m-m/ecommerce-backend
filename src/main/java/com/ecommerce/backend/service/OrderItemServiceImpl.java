package com.ecommerce.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.repos.OrderItemRepo;

@Service
public class OrderItemServiceImpl implements OrderItemService{

	@Autowired
	private OrderItemRepo orderItemRepo;
	
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
	    return orderItemRepo.save(orderItem);
	}

	@Override
	public OrderItem findByOrderItemId(Long orderItemId) {
		try {
			Optional<OrderItem> opt = orderItemRepo.findById(orderItemId);
			if(opt.isPresent()) {
				return opt.get();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}


}
