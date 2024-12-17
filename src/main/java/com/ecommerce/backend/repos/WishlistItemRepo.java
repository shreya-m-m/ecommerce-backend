package com.ecommerce.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.model.WishlistItem;

@Repository
public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {

    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist = :wishlist AND wi.product = :product AND wi.productId = :productId AND wi.size = :size AND wi.userId = :userId")
   public  WishlistItem isWishlistItemExist(@Param("wishlist") Wishlist wishlist, 
                                     @Param("product") Product product, 
                                     @Param("productId") Long productId,
                                     @Param("size") String size, 
                                     @Param("userId") Long userId);
}
