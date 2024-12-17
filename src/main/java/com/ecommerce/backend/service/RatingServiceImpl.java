package com.ecommerce.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.RatingException;
import com.ecommerce.backend.exception.ReviewException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Ratings;
import com.ecommerce.backend.model.Reviews;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repos.RatingRepo;
import com.ecommerce.backend.request.RatingRequest;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepo ratingRepo;
	@Autowired
	private ProductService productService;
	
	@Override
	public Ratings createRating(RatingRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		
		Ratings rating = new Ratings();
		rating.setUser(user);
		rating.setProduct(product);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		return ratingRepo.save(rating);
	}

	@Override
	public List<Ratings> getProductRatings(Long productId) {
		
		return ratingRepo.getAllProductsRating(productId);
	}

	@Override
	public Ratings findRatingById(Long id) throws RatingException {
		Optional<Ratings> opt = ratingRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new RatingException("Review not found with id"+id);
	}

	@Override
	public String deleteRating(Long ratingId) throws RatingException {
		Ratings rating = findRatingById(ratingId);
		ratingRepo.delete(rating);
		return "Rating Deleted Successfully";
	}

}
