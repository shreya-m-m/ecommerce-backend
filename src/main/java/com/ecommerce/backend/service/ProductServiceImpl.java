package com.ecommerce.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repos.CategoryRepo;
import com.ecommerce.backend.repos.ProductRepo;
import com.ecommerce.backend.request.CreateProductRequest;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryRepo categoryRepo;
	
	
	@Override
	public Product createProduct(CreateProductRequest req) {
		
		Category topLevel = categoryRepo.findByName(req.getTopLavelCategory());
		if(topLevel==null) {
			Category topLavelCategory = new Category();
			topLavelCategory.setName(req.getTopLavelCategory());
			topLavelCategory.setLevel(1);
			topLevel=categoryRepo.save(topLavelCategory);
		}
		
		Category secLevel = categoryRepo.findByNameAndParent(req.getSecondLavelCategory(), topLevel.getName());
		if(secLevel==null) {
			Category secondLavelCategory = new Category();
			secondLavelCategory.setName(req.getSecondLavelCategory());
			secondLavelCategory.setParentCategory(topLevel);
			secondLavelCategory.setLevel(2);
			secLevel=categoryRepo.save(secondLavelCategory);
		}
		
		Category thirdLevel = categoryRepo.findByNameAndParent(req.getThirdLavelCategory(),secLevel.getName());
		if(thirdLevel==null) {
			Category thirdLavelCategory = new Category();
			thirdLavelCategory.setName(req.getThirdLavelCategory());
			thirdLavelCategory.setParentCategory(secLevel);
			thirdLavelCategory.setLevel(3);
			thirdLevel=categoryRepo.save(thirdLavelCategory);
		}
		
		Product product = new Product();
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountPersent(req.getDiscountPersent());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setPrice(req.getPrice());
		product.setSizes(req.getSize());
		product.setQuantity(req.getQuantity());
		product.setCategory(thirdLevel);
		product.setCreatedAt(LocalDateTime.now());
		
		Product saveProduct = productRepo.save(product);
		
		return saveProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		Product product =findProductById(productId);
		product.getSizes().clear();
		productRepo.delete(product);
		return "Product Deleted Successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
	    // Find the product by ID
	    Product product = findProductById(productId);
	    
	    // Check if request has non-null values and update the product fields
	    if (req.getTitle() != null && !req.getTitle().isEmpty()) {
	        product.setTitle(req.getTitle());
	    }
	    
	    if (req.getPrice() != 0 && req.getPrice() > 0) {
	        product.setPrice(req.getPrice());
	    }
	    
	    if (req.getDiscountedPrice() != 0 && req.getDiscountedPrice() > 0) {
	        product.setDiscountedPrice(req.getDiscountedPrice());
	    }
	    
	    if (req.getDiscountPersent() != 0 && req.getDiscountPersent() > 0) {
	        product.setDiscountPersent(req.getDiscountPersent());
	    }
	    if (req.getQuantity() != 0 && req.getQuantity() >= 0) {
	        product.setQuantity(req.getQuantity());
	    }
	    
	    if (req.getColor() != null) {
	        product.setColor(req.getColor());
	    }
	    
	    if (req.getImageUrl() != null && !req.getImageUrl().isEmpty()) {
	        product.setImageUrl(req.getImageUrl());
	    }
	    
	    if (req.getCategory() != null) {
	        product.setCategory(req.getCategory());
	    }
	    
	    if (req.getSizes() != null && !req.getSizes().isEmpty()) {
	        product.setSizes(req.getSizes());
	    }
	    if (req.getDescription() != null) {
	    	product.setDescription(req.getDescription());
	    }

	    // Save the updated product in the repository
	    return productRepo.save(product);
	}


	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt = productRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new ProductException("Product not found with id"+id);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> color, List<String> size, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, Integer pageNumber, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Product> products = productRepo.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		
		if(!color.isEmpty()) {
			products= products.stream().filter(p->color.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
		}
		
		
		int startIndex=(int)pageable.getOffset();
		int endIndex=Math.min(startIndex+pageable.getPageSize(), products.size());
		 
		List<Product>pageContent = products.subList(startIndex, endIndex);
		
		Page<Product> filteredProducts= new PageImpl<>(pageContent,pageable,products.size());
		return filteredProducts;
	}

	@Override
	public List<Product> findAllProducts() {
		// TODO Auto-generated method stub
		return productRepo.findAll();
	}

}
