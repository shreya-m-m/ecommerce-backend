package com.ecommerce.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cartItems_id;
	
	@JsonIgnore
	@ManyToOne
	private Cart cart;
	
	@ManyToOne
	private Product product;
	
	private String size;
	private Integer price;
	private int quantity;
	private Integer discountedPrice;
	private Long userId;
	
	
	public CartItem() {
		super();
	}
	
	public CartItem(Long cartItems_id, Cart cart, Product product, String size, Integer price, int quantity,
			Integer discountedPrice, Long userId) {
		super();
		this.cartItems_id = cartItems_id;
		this.cart = cart;
		this.product = product;
		this.size = size;
		this.price = price;
		this.quantity = quantity;
		this.discountedPrice = discountedPrice;
		this.userId = userId;
	}
	public Long getCartItems_id() {
		return cartItems_id;
	}
	public void setCartItems_id(Long cartItems_id) {
		this.cartItems_id = cartItems_id;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Integer getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Integer discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
