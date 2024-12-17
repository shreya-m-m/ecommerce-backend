package com.ecommerce.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.model.PaymentDetails;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.AddressRepo;
import com.ecommerce.backend.repos.CartRepo;
import com.ecommerce.backend.repos.OrderItemRepo;
import com.ecommerce.backend.repos.OrderRepo;
import com.ecommerce.backend.repos.UserRepo;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressRepo addressRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderItemRepo orderItemRepo;

	@Override
	public MyOrder createOrder(User user, Address shippingAddress) {
	    
	    shippingAddress.setUser(user);
	    Address address = addressRepo.save(shippingAddress);
	    user.getAddress().add(address);
	    userRepo.save(user);

	    Cart cart = cartService.findUserCart(user.getUser_id());
	    List<OrderItem> orderItems = new ArrayList<>();

	    for (CartItem item : cart.getCartItem()) {
	        OrderItem orderItem = new OrderItem();

	        orderItem.setPrice(item.getPrice());
	        orderItem.setProduct(item.getProduct());
	        orderItem.setQuantity(item.getQuantity());
	        orderItem.setSize(item.getSize());
	        orderItem.setUserId(item.getUserId());
	        orderItem.setDiscountedPrice(item.getDiscountedPrice());

	        OrderItem createOrderItem = orderItemRepo.save(orderItem);
	        orderItems.add(createOrderItem);
	    }

	    MyOrder createOrder = new MyOrder();
	    createOrder.setUser(user);
	    createOrder.setOrderItem(orderItems);
	    createOrder.setTotalPrice(cart.getTotalPrice());
	    createOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
	    createOrder.setDiscount(cart.getDiscount());
	    createOrder.setTotalItem(cart.getTotalitems());
	    createOrder.setShippingAddress(address);
	    
	    createOrder.setOrderDate(LocalDateTime.now());
	    createOrder.setOrderStatus("PENDING");
	

	    // Check if PayDetails is null and initialize it if needed
	    if (createOrder.getPaydetails() == null) {
	        createOrder.setPaydetails(new PaymentDetails());
	    }
	    createOrder.getPaydetails().setPayStatus("PENDING");

	    createOrder.setCreatedAt(LocalDateTime.now());
	    MyOrder saveOrder = orderRepo.save(createOrder);

	    for (OrderItem item : orderItems) {
	        item.setOrder(saveOrder);
	        orderItemRepo.save(item);
	    }

	    return saveOrder;
	}


	@Override
	public MyOrder findOrderById(Long orderId) throws OrderException {
		Optional<MyOrder> opt = orderRepo.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("Order Does not exist with id"+ orderId);
	}

	@Override
	public List<MyOrder> userOrderHistory(Long userId) {
		List<MyOrder> orders = orderRepo.getUserOrders(userId);
		return orders;
	}

	@Override
	public MyOrder placeOrder(Long orderId) throws OrderException {
		MyOrder order = findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaydetails().setPayStatus("Completed");
		return order;
	}

	@Override
	public MyOrder confirmOrder(Long orderId) throws OrderException {
	    MyOrder order = findOrderById(orderId);
	    order.setOrderStatus("CONFIRMED");
	    order.setDeliveryDate(LocalDateTime.now().plusDays(4)); 
	    return orderRepo.save(order);
	}


	@Override
	public MyOrder shippedOrder(Long orderId) throws OrderException {
		MyOrder order = findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		return orderRepo.save(order);
	}

	@Override
	public MyOrder deliveredOrder(Long orderId) throws OrderException {
		MyOrder order = findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		  order.setDeliveryDate(LocalDateTime.now()); 
		return orderRepo.save(order);
	}

	@Override
	public MyOrder cancledOrder(Long orderId) throws OrderException {
		MyOrder order = findOrderById(orderId);
		order.setOrderStatus("CANCELLED");
		return orderRepo.save(order);
	}

	@Override
	public List<MyOrder> getAllOrders() {
		
		return orderRepo.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		MyOrder order = findOrderById(orderId);
		orderRepo.deleteById(orderId);
		
	}

}
