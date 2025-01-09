package com.ecommerce.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.WishlistItemException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.repos.WishlistItemRepo;
import com.ecommerce.backend.repos.WishlistRepo;

@Service
public class WishlistItemServiceImpl implements WishlistItemService {

    @Autowired
    private WishlistItemRepo wishlistItemRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WishlistRepo wishlistRepo;
    
    @Override
    public WishlistItem createWishlistItem(WishlistItem wishlistItem) {
        // Setting default quantity as 1
        wishlistItem.setQuantity(1);
        wishlistItem.setPrice(wishlistItem.getProduct().getPrice());
        wishlistItem.setDiscountedPrice(wishlistItem.getProduct().getDiscountedPrice());
        
        WishlistItem createWishlistItem = wishlistItemRepo.save(wishlistItem);
        
        // Saving the WishlistItem to the repository
        return createWishlistItem;
    }

    @Override
    public WishlistItem updateWishlistItem(Long userId, Long id, WishlistItem wishlistItem) throws WishlistItemException, UserException {
        // Fetching the existing WishlistItem by ID
        WishlistItem item = findWishlistItemById(id);
        User user = userService.findUserById(userId);

        // Verifying that the user is authorized to update this item
        if (user.getUser_id().equals(userId)) {
            item.setQuantity(item.getQuantity());
            item.setPrice(item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice());
            
        } else {
            throw new UserException("You do not have permission to update this wishlist item.");
        }

        // Saving the updated WishlistItem to the repository
        return wishlistItemRepo.save(item);
    }

    @Override
    public WishlistItem isWishlistItemExist(Wishlist wishlist, Product product,String size, Long userId) {
        // Checking if a wishlist item already exists in the given wishlist
        return wishlistItemRepo.isWishlistItemExist(wishlist, product, size, userId);
    }

    @Override
    public void removeWishlistItem(Long userId, Long wishlistItemId) throws WishlistItemException, UserException {
        // Fetching the WishlistItem to be removed
        WishlistItem wishlistItem = findWishlistItemById(wishlistItemId);
        User user = userService.findUserById(wishlistItem.getUserId());

        // Verifying that the user is authorized to remove this item
        if (user.getUser_id().equals(userId)) {
            wishlistItemRepo.deleteById(wishlistItemId);
        } else {
            throw new UserException("You cannot remove another user's wishlist item.");
        }
    }

    @Override
    public WishlistItem findWishlistItemById(Long wishlistItemId) throws WishlistItemException {
        // Fetching a WishlistItem by ID
        Optional<WishlistItem> opt = wishlistItemRepo.findById(wishlistItemId);

        // If found, return it; otherwise, throw an exception
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new WishlistItemException("WishlistItem not found with id: " + wishlistItemId);
    }

	
}
