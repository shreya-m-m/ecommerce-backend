package com.ecommerce.backend.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.PaymentDetails;
import com.ecommerce.backend.model.PaymentInfo;
import com.ecommerce.backend.repos.OrderRepo;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.PaymentLinkResponse;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    String apiKey;

    @Value("${razorpay.api.secret}")
    String apiSecret;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws OrderException {
        MyOrder order = orderService.findOrderById(orderId);
        
        System.out.println("Im Here Inside the Payment controller ");

        try {
        	
        	System.out.println("Im Here Inside the Payment controller try");
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstname());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);
            
            System.out.println("Customer name "+ order.getUser().getFirstname());
            System.out.println("Customer Email "+order.getUser().getEmail());
            

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "https://trendinsta.vercel.app/payment/"+orderId);
//            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/"+orderId);
            paymentLinkRequest.put("callback_method", "get");
            
            System.out.println("Payment Request Link "+ paymentLinkRequest );

            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");
            
            System.out.println("paymentLinkId "+ paymentLinkId );
            System.out.println("paymentLinkUrl "+paymentLinkUrl);

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPaymentLinkId(paymentLinkId);
            res.setPaymentLinkUrl(paymentLinkUrl);

            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);

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
            System.out.println("Payment Details: " + payment);

            // Extract card details from the nested card object
            JSONObject cardDetails = (JSONObject) payment.get("card");
            String cardHolderName = null, cardLast4 = null, cardType = null, cardNetwork = null, expiryMonths = null, expiryYears = null;
            
            if (cardDetails != null) {
                cardHolderName = cardDetails.optString("name", "N/A");
                cardLast4 = cardDetails.optString("last4", "N/A");
                cardType = cardDetails.optString("type", "N/A");
                cardNetwork = cardDetails.optString("network", "N/A");
                expiryMonths = cardDetails.optString("expiry_month", "N/A");
                expiryYears = cardDetails.optString("expiry_year", "N/A");

//                // Print out the extracted information
                System.out.println("Cardholder Name: " + cardHolderName);
                System.out.println("Card Last 4 Digits: " + cardLast4);
                System.out.println("Card Type: " + cardType);
                System.out.println("Card Network: " + cardNetwork);
                System.out.println("Expiration Month: " + expiryMonths);
                System.out.println("Expiration Year: " + expiryYears);
            } else {
                System.out.println("No card details available in the payment.");
            }

            // Extract payment method and verify payment status
            String paymentMethod = payment.get("method");
            
            
            if ("captured".equals(payment.get("status"))) {
                // Create and populate PaymentInfo object
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setCardholderName(cardHolderName);
                paymentInfo.setCardNumber(cardLast4);

                // Set expiration date
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

                // Add PaymentInfo to User's paymentInfo list
                order.getUser().getPaymentInfo().add(paymentInfo);

                // Update order details
                order.getPaydetails().setPayId(paymentId);
                order.getPaydetails().setPayStatus("COMPLETED");
                order.getPaydetails().setPaymentMethod(paymentMethod);
                order.setOrderStatus("PLACED");

                // Save the updated order and user details
                orderRepo.save(order);

                ApiResponse response = new ApiResponse("Order Placed Successfully", true);
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else {
                // Payment not captured scenario
                ApiResponse response = new ApiResponse("Payment not captured", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RazorpayException e) {
            // Handle Razorpay API specific errors
            System.err.println("Razorpay Exception: " + e.getMessage());
            return new ResponseEntity<>(new ApiResponse("Payment processing failed: " + e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Handle any other generic exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(new ApiResponse("Unexpected error occurred: " + e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
