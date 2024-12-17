package com.ecommerce.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.WishlistItemException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.WishlistItemService;
import com.ecommerce.backend.service.UserService;

@RestController
@RequestMapping("/api/wishlist_item")
public class WishlistItemController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private WishlistItemService wishlistItemService;

    // Remove item from wishlist
    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<ApiResponse> deleteWishlistItem(@PathVariable Long wishlistItemId, @RequestHeader("Authorization") String jwt) throws UserException, WishlistItemException {
        User user = userService.findUserProfileByJwtId(jwt);
        wishlistItemService.removeWishlistItem(user.getUser_id(), wishlistItemId);
        
        ApiResponse res = new ApiResponse();
        res.setMsg("Item Removed From Wishlist");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // Update an item in the wishlist
    @PutMapping("/{wishlistItemId}")
    public ResponseEntity<WishlistItem> updateWishlistItem(@RequestBody WishlistItem wishlistItem, @PathVariable Long wishlistItemId, @RequestHeader("Authorization") String jwt) throws UserException, WishlistItemException {
        User user = userService.findUserProfileByJwtId(jwt);
        
        WishlistItem updatedWishlistItem = wishlistItemService.updateWishlistItem(user.getUser_id(), wishlistItemId, wishlistItem);
        return new ResponseEntity<>(updatedWishlistItem, HttpStatus.OK);
    }
}
