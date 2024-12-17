package com.ecommerce.backend.controller;

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

import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishlistItemService;
import com.ecommerce.backend.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;
    @Autowired
    private WishlistItemService wishlistItemService;
    @Autowired
    private ProductService productService;

    // Get user's wishlist
    @GetMapping("/")
    public ResponseEntity<Wishlist> findUserWishlist(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwtId(jwt);
        Wishlist wishlist = wishlistService.findUserWishlist(user.getUser_id());
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    // Add item to user's wishlist
    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToWishlist(
            @RequestBody AddItemRequest req, 
            @RequestHeader("Authorization") String jwt
    ) throws UserException, ProductException {
        // Retrieve the user using the JWT token
        User user = userService.findUserProfileByJwtId(jwt);

        // Find the wishlist of the user (assuming `wishlistService` has a method to get the user's wishlist)
        Wishlist wishlist = wishlistService.findUserWishlist(user.getUser_id());
        
        // Fetch the product using productId from the request
        Product product = productService.findProductById(req.getProductId()); // Assuming a method to fetch the product by ID
        
//        // Check if the item already exists in the wishlist by Product object, productId, size, and userId
//        WishlistItem existingItem = wishlistItemService.isWishlistItemExist(
//            wishlist, 
//            product, 
//            req.getProductId(), 
//            req.getSize(), 
//            user.getUser_id()
//        );
//
//        // If existingItem is not null, return an error indicating the item is already in the wishlist
//        if (existingItem != null) {
//            ApiResponse res = new ApiResponse();
//            res.setMsg("Item already exists in the wishlist");
//            res.setStatus(false);
//            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);  // Return a 400 status
//        }
        
        // If the product does not exist, add it to the wishlist
        wishlistService.addWishlistItem(user.getUser_id(), req, product.getProduct_id());
        
        ApiResponse res = new ApiResponse();
        res.setMsg("Item Added to Wishlist Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }



 // Add item to cart from wishlist
    @PostMapping("/add-to-cart")
    public ResponseEntity<ApiResponse> addProductToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwtId(jwt);
        cartService.addCartItem(user.getUser_id(), req); // Call CartService

        ApiResponse res = new ApiResponse();
        res.setMsg("Item Added to Cart Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
