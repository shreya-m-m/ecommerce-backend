package com.ecommerce.backend.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.PaymentInfo;
import com.ecommerce.backend.repos.OrderRepo;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.PaymentLinkResponse;
import com.ecommerce.backend.service.OrderService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Value("${app.base.url}")
    private String baseUrl; 
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
                                                                 @RequestHeader("Authorization") String jwt) throws OrderException {
        MyOrder order = orderService.findOrderById(orderId);

        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100); // Amount in paise
            paymentLinkRequest.put("currency", "INR");
            String callbackUrl = "https://trendinsta.vercel.app/payment/" + orderId;
            paymentLinkRequest.put("callback_url", callbackUrl);
            paymentLinkRequest.put("callback_method", "get");

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstname());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPaymentLinkId(paymentLinkId);
            res.setPaymentLinkUrl(paymentLinkUrl);

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new OrderException("Unable to create payment link. Please try again later.");
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
                                                @RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException {
        MyOrder order = orderService.findOrderById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

        try {
            // Fetch payment details from Razorpay
            Payment payment = razorpay.payments.fetch(paymentId);

            JSONObject cardDetails = (JSONObject) payment.get("card");
            String cardHolderName = null, cardLast4 = null, cardType = null, cardNetwork = null, expiryMonths = null, expiryYears = null;
            
            if (cardDetails != null) {
                cardHolderName = cardDetails.optString("name", "N/A");
                cardLast4 = cardDetails.optString("last4", "N/A");
                cardType = cardDetails.optString("type", "N/A");
                cardNetwork = cardDetails.optString("network", "N/A");
                expiryMonths = cardDetails.optString("expiry_month", "N/A");
                expiryYears = cardDetails.optString("expiry_year", "N/A");
            }

            String paymentMethod = payment.get("method");
            
            if ("captured".equals(payment.get("status"))) {
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setCardholderName(cardHolderName);
                paymentInfo.setCardNumber(cardLast4);

                try {
                    if (!"N/A".equals(expiryMonths) && !"N/A".equals(expiryYears)) {
                        int month = Integer.parseInt(expiryMonths);
                        int year = Integer.parseInt(expiryYears);
                        LocalDateTime expirationDate = LocalDateTime.of(year, month, 1, 0, 0);
                        paymentInfo.setExpirationDate(expirationDate);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing expiration date: " + e.getMessage());
                }

                order.getUser().getPaymentInfo().add(paymentInfo);

                order.getPaydetails().setPayId(paymentId);
                order.getPaydetails().setPayStatus("COMPLETED");
                order.getPaydetails().setPaymentMethod(paymentMethod);
                order.setOrderStatus("PLACED");

                orderRepo.save(order);

                ApiResponse response = new ApiResponse("Order Placed Successfully", true);
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else {
                ApiResponse response = new ApiResponse("Payment not captured", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RazorpayException e) {
            return new ResponseEntity<>(new ApiResponse("Payment processing failed: " + e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Unexpected error occurred: " + e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
