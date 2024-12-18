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

		public String getCardholderName() {
			return cardholderName;
		}

		public void setCardholderName(String cardholderName) {
			this.cardholderName = cardholderName;
		}

		public String getCardNumber() {
			return cardNumber;
		}

		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}

		public LocalDateTime getExpirationDate() {
			return expirationDate;
		}

		public void setExpirationDate(LocalDateTime expirationDate) {
			this.expirationDate = expirationDate;
		}

		public String getCvv() {
			return cvv;
		}

		public void setCvv(String cvv) {
			this.cvv = cvv;
		}
		
		
		
		
	

}
