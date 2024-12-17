package com.ecommerce.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;


@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long user_id;
	
	private String firstname;
	private String lastname;
	private String password;
	 private String email;
	 private String role;
	 private String phone_number;
	 
	 @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	 private List<Address> address = new ArrayList<>();
	 
	 @Embedded
	 @ElementCollection
	 @CollectionTable(name="payment_info", joinColumns = @JoinColumn(name="user_id"))
	 private List<PaymentInfo> paymentInfo = new ArrayList<>();
	 
	 @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	 @JsonIgnore
	 private List<Ratings>ratings = new ArrayList<>();
	 
	 @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	 @JsonIgnore
	 private List<Reviews>reviews = new ArrayList<>();
	 
	 private LocalDateTime createdAt;

	public User() {
		super();
	}

	public User(Long user_id, String firstname, String lastname, String password, String email, String role,
			String phone_number, List<Address> address, List<PaymentInfo> paymentInfo, List<Ratings> ratings,
			List<Reviews> reviews, LocalDateTime createdAt) {
		super();
		this.user_id = user_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.email = email;
		this.role = role;
		this.phone_number = phone_number;
		this.address = address;
		this.paymentInfo = paymentInfo;
		this.ratings = ratings;
		this.reviews = reviews;
		this.createdAt = createdAt;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<PaymentInfo> getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(List<PaymentInfo> paymentInfo) {
		this.paymentInfo = paymentInfo;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	 
	 
	 
}
