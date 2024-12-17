package com.ecommerce.backend.model;

import java.time.LocalDateTime;

import javax.persistence.Column;

public class PaymentInfo {
	@Column(name="cardholder_name")
	private String cardholderName;
	
	@Column(name="cardh_number")
	private String cardNumber;
	
	@Column(name="expiration_date")
	private LocalDateTime expirationDate;
	
	@Column(name="cvv")
	private String cvv;
	
	

}
