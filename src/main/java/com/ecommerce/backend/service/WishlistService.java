package com.ecommerce.backend.service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.request.AddItemRequest;

public interface WishlistService {

    // Method to create a wishlist for a user
    public Wishlist createWishlist(User user);

    // Method to add an item to the user's wishlist
    public String addWishlistItem(Long userId, AddItemRequest req, Long productId) throws ProductException;

    // Method to find the user's wishlist
    public Wishlist findUserWishlist(Long userId);
}
