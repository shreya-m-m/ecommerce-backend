package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.WishlistItemException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;

public interface WishlistItemService {

    // Method to create a new WishlistItem
    public WishlistItem createWishlistItem(WishlistItem wishlistItem);

    // Method to update an existing WishlistItem
    public WishlistItem updateWishlistItem(Long userId, Long id, WishlistItem wishlistItem) 
        throws WishlistItemException, UserException;

    // Method to check if a WishlistItem already exists in the wishlist
    public WishlistItem isWishlistItemExist(Wishlist wishlist, Product product, String size, Long userId);

    // Method to remove a WishlistItem from the user's wishlist
    public void removeWishlistItem(Long userId, Long wishlistItemId) 
        throws WishlistItemException, UserException;

    // Method to find a WishlistItem by its ID
    public WishlistItem findWishlistItemById(Long wishlistItemId) throws WishlistItemException;

   
}
