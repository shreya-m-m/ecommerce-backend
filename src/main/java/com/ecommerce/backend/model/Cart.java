package com.ecommerce.backend.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cart_id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	@OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval=true)
	@Column(name="cart_items")
	private Set<CartItem> cartItem = new HashSet<>();
	
	@Column(name="total_price")
	private int totalPrice;
	
	@Column(name="total_items")
	private int totalitems;
	
	private int totalDiscountedPrice;
	
	private int discount;
	

	public Cart() {
		super();
	}

	
	
	public Cart(Long cart_id, User user, Set<CartItem> cartItem, int totalPrice, int totalitems,
			int totalDiscountedPrice, int discount) {
		super();
		this.cart_id = cart_id;
		this.user = user;
		this.cartItem = cartItem;
		this.totalPrice = totalPrice;
		this.totalitems = totalitems;
		this.totalDiscountedPrice = totalDiscountedPrice;
		this.discount = discount;
	}



	public Long getCart_id() {
		return cart_id;
	}

	public void setCart_id(Long cart_id) {
		this.cart_id = cart_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(Set<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getTotalitems() {
		return totalitems;
	}

	public void setTotalitems(int totalitems) {
		this.totalitems = totalitems;
	}

	public int getTotalDiscountedPrice() {
		return totalDiscountedPrice;
	}

	public void setTotalDiscountedPrice(int totalDiscountedPrice) {
		this.totalDiscountedPrice = totalDiscountedPrice;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	
	
	
	
}
