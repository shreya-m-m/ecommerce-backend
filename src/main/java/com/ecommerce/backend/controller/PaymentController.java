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
import com.ecommerce.backend.repos.OrderRepo;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.PaymentLinkResponse;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Value("${app.callback.url}")
    private String callbackBaseUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId) throws OrderException {
        MyOrder order = orderService.findOrderById(orderId);
        if (order == null) {
            throw new OrderException("Order not found with ID: " + orderId);
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100); // Amount in paise
            paymentLinkRequest.put("currency", "INR");

            // Customer details
            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstname());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            // Notification options
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Callback URL
            String callbackUrl = callbackBaseUrl + "/payment/" + orderId;
            paymentLinkRequest.put("callback_url", callbackUrl);
            paymentLinkRequest.put("callback_method", "get");

            // Create payment link
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            // Prepare response
            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPaymentLinkId(payment.get("id"));
            res.setPaymentLinkUrl(payment.get("short_url"));

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (RazorpayException e) {
            throw new OrderException("Unable to create payment link: " + e.getMessage());
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
            @RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException {
        MyOrder order = orderService.findOrderById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

        try {
            Payment payment = razorpay.payments.fetch(paymentId);

            if ("captured".equals(payment.get("status"))) {
                // Update order payment details
                order.getPaydetails().setPayId(paymentId);
                order.getPaydetails().setPayStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRepo.save(order);

                ApiResponse response = new ApiResponse();
                response.setMsg("Order Placed Successfully");
                response.setStatus(true);
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else {
                ApiResponse response = new ApiResponse("Payment not captured", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RazorpayException(e.getMessage());
        }
    }
}
