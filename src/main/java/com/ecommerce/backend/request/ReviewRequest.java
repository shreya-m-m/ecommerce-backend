package com.ecommerce.backend.request;

public class ReviewRequest {
	
	private Long productId;
	private String review;
	
	
	public ReviewRequest() {
		super();
	}
	public ReviewRequest(Long productId, String review) {
		super();
		this.productId = productId;
		this.review = review;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
	

}
