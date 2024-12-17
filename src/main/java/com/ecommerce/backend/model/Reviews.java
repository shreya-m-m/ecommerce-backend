package com.ecommerce.backend.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Reviews {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long review_id;
	
	private String review;
	
	 @ManyToOne
	 @JoinColumn(name="product_id")
	 private Product product;
	 
	 @ManyToOne
	 @JoinColumn(name="user_id")
	 private User user;
	 
	 private LocalDateTime createdAt;

	public Reviews() {
		super();
	}

	public Reviews(Long review_id, String review, Product product, User user, LocalDateTime createdAt) {
		super();
		this.review_id = review_id;
		this.review = review;
		this.product = product;
		this.user = user;
		this.createdAt = createdAt;
	}

	public Long getReview_id() {
		return review_id;
	}

	public void setReview_id(Long review_id) {
		this.review_id = review_id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	 
	 
	 

}
