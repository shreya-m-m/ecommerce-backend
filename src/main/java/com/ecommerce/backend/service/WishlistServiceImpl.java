package com.ecommerce.backend.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.repos.WishlistRepo;
import com.ecommerce.backend.repos.WishlistItemRepo;
import com.ecommerce.backend.request.AddItemRequest;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;
    
    @Autowired
    private WishlistItemRepo wishlistItemRepo;
    
    @Autowired
    private WishlistItemService wishlistItemService;
    
    @Autowired
    private ProductService productService;
    
    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepo.save(wishlist);
    }

    @Override
    public String addWishlistItem(Long userId, AddItemRequest req, Long productId) throws ProductException {


        Wishlist wishlist = wishlistRepo.findByUserId(userId);
        // Find the product by ID
        Product product = productService.findProductById(productId);
        if (product == null) {
            throw new ProductException("Product with ID " + productId + " does not exist");
        }

        // Check if an identical item already exists in the wishlist
        boolean isDuplicate = wishlist.getWishlistItems().stream()
            .anyMatch(item -> item.getProduct().getProduct_id().equals(productId) &&
                              item.getUserId().equals(userId));

        if (isDuplicate) {
            return "Item already exists in the wishlist with the same size";
        }

        // Create a new wishlist item if it doesn't exist
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setQuantity(req.getQuantity());
        wishlistItem.setProductId(productId);
        wishlistItem.setUserId(userId);
        wishlistItem.setPrice(req.getPrice());
        if (req.getSize() != null && !req.getSize().isEmpty()) {
        	wishlistItem.setSize(req.getSize());
		} else {
			wishlistItem.setSize(null); 
		}

        // Save the new wishlist item
        WishlistItem createdWishlistItem = wishlistItemService.createWishlistItem(wishlistItem);

        // Add the new item to the wishlist and save the updated wishlist
        wishlist.getWishlistItems().add(createdWishlistItem);
        wishlistRepo.save(wishlist);

        return "Item successfully added to wishlist";
    }


    @Override
    public Wishlist findUserWishlist(Long userId) {
        Wishlist wishlist = wishlistRepo.findByUserId(userId);
        int totalItem=0;
        for(WishlistItem wishlistItem :wishlist.getWishlistItems()) {
        	totalItem=totalItem+wishlistItem.getQuantity();
        }
        wishlist.setTotalItems(totalItem);
        return wishlistRepo.save(wishlist);
    }
}
