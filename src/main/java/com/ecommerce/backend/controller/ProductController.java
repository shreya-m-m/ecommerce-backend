package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	

@GetMapping("/product")
public ResponseEntity<Page<Product>>findProductByCategory(@RequestParam String category,@RequestParam List<String> color,@RequestParam List<String> size,@RequestParam Integer minPrice,@RequestParam Integer maxPrice,@RequestParam Integer minDiscount,@RequestParam String sort,@RequestParam Integer pageNumber,@RequestParam Integer pageSize,@RequestHeader("Authorization")String jwt) throws UserException{
	Page<Product> res = productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, pageNumber, pageSize);
	System.out.println("Complete Products");
	
	return new  ResponseEntity<>(res,HttpStatus.ACCEPTED);
	
}
@GetMapping("/product/product_id/{productId}")
public ResponseEntity<Product>findProductByIdHandler(@PathVariable Long productId) throws ProductException{
	Product product = productService.findProductById(productId);
	
	return new  ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
	
}



}
