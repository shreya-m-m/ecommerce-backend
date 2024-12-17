package com.ecommerce.backend.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderItem_id;

	@JsonIgnore
	@ManyToOne
	private MyOrder order;
	
	@ManyToOne
	private Product product;
	
	private String size;
	private Integer price;
	private int quantity;
	private Integer discountedPrice;
	private Long userId;
	private LocalDateTime deliveryDate;
	
	
	
	public OrderItem() {
		super();
	}
	
	
	public OrderItem(Long orderItem_id, MyOrder order, Product product, String size, Integer price, int quantity,
			Integer discountedPrice, Long userId, LocalDateTime deliveryDate) {
		super();
		this.orderItem_id = orderItem_id;
		this.order = order;
		this.product = product;
		this.size = size;
		this.price = price;
		this.quantity = quantity;
		this.discountedPrice = discountedPrice;
		this.userId = userId;
		this.deliveryDate = deliveryDate;
	}


	public Long getOrderItem_id() {
		return orderItem_id;
	}
	public void setOrderItem_id(Long orderItem_id) {
		this.orderItem_id = orderItem_id;
	}
	public MyOrder getOrder() {
		return order;
	}
	public void setOrder(MyOrder order) {
		this.order = order;
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
	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	
}
