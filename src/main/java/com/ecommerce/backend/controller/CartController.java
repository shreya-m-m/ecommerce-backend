package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishlistService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WishlistService wishlistService;
	
    @Autowired
    private ProductService productService;
	
	@GetMapping("/")
	public ResponseEntity<Cart>findUserCart(@RequestHeader("Authorization")String jwt) throws UserException{
		User user = userService.findUserProfileByJwtId(jwt);
		Cart cart = cartService.findUserCart(user.getUser_id());
		return new  ResponseEntity<Cart>(cart,HttpStatus.OK);
		
	}
	
	@PutMapping("/add")
	public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,@RequestHeader("Authorization")String jwt) throws UserException, ProductException{
		User user = userService.findUserProfileByJwtId(jwt);
		cartService.addCartItem(user.getUser_id(), req);
		ApiResponse res = new ApiResponse();
		res.setMsg("Item Added to Cart Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
	@PostMapping("/add-to-wishlist")
	public ResponseEntity<ApiResponse> addProductToWishlist(
	        @RequestBody AddItemRequest req, 
	        @RequestHeader("Authorization") String jwt) {
	    try {
	        // Get the user information using JWT token
	        User user = userService.findUserProfileByJwtId(jwt);

	        // Retrieve the product details
	        Product product = productService.findProductById(req.getProductId());

	        // Check if the product already exists in the wishlist
	        String result = wishlistService.addWishlistItem(user.getUser_id(), req, product.getProduct_id());
	        
	        ApiResponse res = new ApiResponse();
	        
	        // If the item already exists, return a specific message
	        if (result.equals("Item already exists in the wishlist")) {
	            res.setMsg("Item already exists in the wishlist");
	            res.setStatus(false);
	            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST); // 400 for bad request
	        } else {
	            // If the item is successfully added
	            res.setMsg("Item Added to Wishlist Successfully");
	            res.setStatus(true);
	            return new ResponseEntity<>(res, HttpStatus.OK); // 200 for success
	        }
	    } catch (Exception e) {
	        // Log the error
	        e.printStackTrace();
	        ApiResponse res = new ApiResponse();
	        res.setMsg("Failed to add item to wishlist: " + e.getMessage());
	        res.setStatus(false);
	        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR); // 500 for server error
	    }
	}


}
