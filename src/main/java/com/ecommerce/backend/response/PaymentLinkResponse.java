package com.ecommerce.backend.response;

public class PaymentLinkResponse {

    private String paymentLinkId;
    private String paymentLinkUrl;


    public PaymentLinkResponse() {}

   
    public String getPaymentLinkId() {
        return paymentLinkId;
    }

    public void setPaymentLinkId(String paymentLinkId) {
        this.paymentLinkId = paymentLinkId;
    }

    
    public String getPaymentLinkUrl() {
        return paymentLinkUrl;
    }

    public void setPaymentLinkUrl(String paymentLinkUrl) {
        this.paymentLinkUrl = paymentLinkUrl;
    }

 
}
