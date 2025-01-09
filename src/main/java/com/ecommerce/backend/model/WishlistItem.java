package com.ecommerce.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistItems_id;

    @JsonIgnore
    @ManyToOne
    private Wishlist wishlist;

    @ManyToOne
    private Product product;

    private String size;
    private Integer price;
    private int quantity;
    private Integer discountedPrice;
    
    private Long userId;
//    private Long productId;



    public WishlistItem() {
        super();
    }


	public WishlistItem(Long wishlistItems_id, Wishlist wishlist, Product product, String size, Integer price,
			int quantity, Integer discountedPrice, Long userId,Long productId) {
		super();
		this.wishlistItems_id = wishlistItems_id;
		this.wishlist = wishlist;
		this.product = product;
		this.size = size;
		this.price = price;
		this.quantity = quantity;
		this.discountedPrice = discountedPrice;
		this.userId = userId;
//		this.productId=productId;
	}


	public Long getWishlistItems_id() {
		return wishlistItems_id;
	}


	public void setWishlistItems_id(Long wishlistItems_id) {
		this.wishlistItems_id = wishlistItems_id;
	}


	public Wishlist getWishlist() {
		return wishlist;
	}


	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
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


//	public Long getProductId() {
//		return productId;
//	}
//
//
//	public void setProductId(Long productId) {
//		this.productId = productId;
//	}
	
    
    

}