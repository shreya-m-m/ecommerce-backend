package com.ecommerce.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.CartRepo;
import com.ecommerce.backend.request.AddItemRequest;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private CartItemService cartItems;
	@Autowired
	private ProductService productService;
	
	@Override
	public Cart createCart(User user) {
		
		Cart cart = new Cart();
		cart.setUser(user);
		
		return cartRepo.save(cart);
	}

	@Override
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
		
		Cart cart = cartRepo.findByUserId(userId);
		Product product = productService.findProductById(req.getProductId());
		
		CartItem isPresent = cartItems.isCartIteExist(cart, product, req.getSize(), userId);
		
		if(isPresent==null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			
			int price=req.getQuantity()*product.getDiscountedPrice();
			
			cartItem.setPrice(price);
			if (req.getSize() != null && !req.getSize().isEmpty()) {
			    cartItem.setSize(req.getSize());
			} else {
			    cartItem.setSize(null); 
			}
			
			CartItem createCartItem = cartItems.createCartItem(cartItem);
			cart.getCartItem().add(createCartItem);
		}
		return "Item Successfully Added To Cart";
	}

	@Override
	public Cart findUserCart(Long userId) {
		Cart cart = cartRepo.findByUserId(userId);
		int totalPrice= 0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		
		for(CartItem cartItem :cart.getCartItem()) {
			totalPrice= totalPrice+cartItem.getPrice();
			totalDiscountedPrice= totalDiscountedPrice+cartItem.getDiscountedPrice();
			totalItem=totalItem+cartItem.getQuantity();
			
		}
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalitems(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscount(totalPrice-totalDiscountedPrice);
		
		return cartRepo.save(cart);
	}

}
