package com.ecommerce.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long product_id;
	
	@Column(name="Title")
	private String title;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="Price")
	private int price;
	
	@Column(name="Discount_price")
	private int discountedPrice;
	
	@Column(name="Discount_percent")
	private int discountPersent;
	
	@Column(name="Quantity")
	private int quantity;
	
	@Column(name="Brand")
	private String brand;
	
	@Column(name="Color")
	private String color;
	
	@Embedded
	@ElementCollection
	@Column(name="Sizes")
	private Set<Size> sizes = new HashSet<>();
	
	@Column(name="IMG_URL")
	private String imageUrl;
	
	 @OneToMany(mappedBy="product",cascade=CascadeType.ALL, orphanRemoval= true)
	 @JsonIgnore
	 private List<Ratings>ratings = new ArrayList<>();
	 
	 @OneToMany(mappedBy="product",cascade=CascadeType.ALL, orphanRemoval= true)
	 @JsonIgnore
	 private List<Reviews>reviews = new ArrayList<>();
	 
	 @Column(name="Num_rating")
	 private int numRating;
	 
	 @ManyToOne
	 @JoinColumn(name="category_id")
	 private Category category;
	 
	 private LocalDateTime createdAt;

	public Product() {
		super();
	}

	public Product(Long product_id, String title, String description, int price, int discountedPrice,
			int discountPersent, int quantity, String brand, String color, Set<Size> sizes, String imageUrl,
			List<Ratings> ratings, List<Reviews> reviews, int numRating, Category category, LocalDateTime createdAt) {
		super();
		this.product_id = product_id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.discountPersent = discountPersent;
		this.quantity = quantity;
		this.brand = brand;
		this.color = color;
		this.sizes = sizes;
		this.imageUrl = imageUrl;
		this.ratings = ratings;
		this.reviews = reviews;
		this.numRating = numRating;
		this.category = category;
		this.createdAt = createdAt;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getDiscountPersent() {
		return discountPersent;
	}

	public void setDiscountPersent(int discountPersent) {
		this.discountPersent = discountPersent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Set<Size> getSizes() {
		return sizes;
	}

	public void setSizes(Set<Size> sizes) {
		this.sizes = sizes;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Ratings> getRatings() {
		return ratings;
	}

	public void setRatings(List<Ratings> ratings) {
		this.ratings = ratings;
	}

	public List<Reviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}

	public int getNumRating() {
		return numRating;
	}

	public void setNumRating(int numRating) {
		this.numRating = numRating;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	
	 
	
}
