//package com.ecommerce.backend.repository;
//
//import com.ecommerce.backend.model.Wishlist;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
//    List<Wishlist> findByUser_UserId(Long userId);
//    void deleteByUser_UserIdAndProductId(Long userId, Long productId);
//}
