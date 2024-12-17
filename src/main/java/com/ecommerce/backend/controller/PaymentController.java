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
	String apiKey;

	@Value("${razorpay.api.secret}")
	String apiSecret;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderRepo orderRepo;

	@PostMapping("payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
	        @RequestHeader("Authorization") String jwt) throws OrderException {
	    MyOrder order = orderService.findOrderById(orderId);

	    try {
	        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

	        JSONObject paymentLinkRequest = new JSONObject();
	        paymentLinkRequest.put("amount", order.getTotalDiscountedPrice()* 100); 
	        paymentLinkRequest.put("currency", "INR");

	        JSONObject customer = new JSONObject();
	        customer.put("name", order.getUser().getFirstname());
	        customer.put("email", order.getUser().getEmail());
	        paymentLinkRequest.put("customer", customer);

	        JSONObject notify = new JSONObject();
	        notify.put("sms", true);
	        notify.put("email", true);
	        paymentLinkRequest.put("notify", notify);

	        paymentLinkRequest.put("callback_url", "https://trendinsta.vercel.app/payment/" + orderId);
	        paymentLinkRequest.put("callback_method", "get");

	        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

	        String paymentLinkId = payment.get("id");
	        String paymentLinkUrl = payment.get("short_url"); 

	        PaymentLinkResponse res = new PaymentLinkResponse();
	        res.setPaymentLinkId(paymentLinkId);
	        res.setPaymentLinkUrl(paymentLinkUrl);

	        return new ResponseEntity<>(res, HttpStatus.CREATED);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new OrderException("Unable to create payment link. Please try again later.");
	    }
	}

	

	@GetMapping("/payments")
	public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
			@RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException {
		MyOrder order = orderService.findOrderById(orderId);
		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

		try {
			Payment payment = razorpay.payments.fetch(paymentId);

			if (payment.get("status").equals("captured")) {

				order.getPaydetails().setPayId(paymentId);
				order.getPaydetails().setPayStatus("COMPLETED");
				order.setOrderStatus("PLACED");
				orderRepo.save(order); 

				ApiResponse response = new ApiResponse();
				response.setMsg("Order Placed Successfully");
				response.setStatus(true);
				return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);
			} else {
				ApiResponse response = new ApiResponse("Payment not captured", false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
		
			throw new RazorpayException(e.getMessage());
		}
	}

}
