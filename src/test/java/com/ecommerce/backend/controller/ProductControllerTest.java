package com.ecommerce.backend.controller;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindProductByCategory() throws Exception {
        // Given
        String category = "Clothing";
        List<String> color = Arrays.asList("Black", "White");
        List<String> size = Arrays.asList("L", "M");
        Integer minPrice = 500;
        Integer maxPrice = 5000;
        Integer minDiscount = 10;
        String sort = "price,asc";
        Integer pageNumber = 0;
        Integer pageSize = 10;
        String jwt = "Bearer valid-token";

        Product product1 = new Product(1L, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());
        Product product2 = new Product(1L, "top", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());

        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));

        when(productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, pageNumber, pageSize))
                .thenReturn(productPage);

        // When
        ResponseEntity<Page<Product>> response = productController.findProductByCategory(
                category, color, size, minPrice, maxPrice, minDiscount, sort, pageNumber, pageSize, jwt);

        // Print Expected and Actual Outputs
        System.out.println("Expected: HttpStatus.ACCEPTED, 2 products: [Dress, top]");
        System.out.println("Actual: " + response.getStatusCode() + ", " + response.getBody().getContent().size() + " products: " +
                response.getBody().getContent().get(0).getTitle() + ", " + response.getBody().getContent().get(1).getTitle());

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Dress", response.getBody().getContent().get(0).getTitle());
        assertEquals("top", response.getBody().getContent().get(1).getTitle());

        verify(productService, times(1)).getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, pageNumber, pageSize);
    }

    @Test
    public void testFindProductByIdHandler() throws ProductException {
        // Given
        Long productId = 1L;
        Product product = new Product(productId, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());

        when(productService.findProductById(productId)).thenReturn(product);

        // When
        ResponseEntity<Product> response = productController.findProductByIdHandler(productId);

        // Print Expected and Actual Outputs
        System.out.println("Expected: HttpStatus.ACCEPTED, Product ID: 1, Title: Dress");
        System.out.println("Actual: " + response.getStatusCode() + ", Product ID: " + response.getBody().getProduct_id() +
                ", Title: " + response.getBody().getTitle());

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getProduct_id());
        assertEquals("Dress", response.getBody().getTitle());

        verify(productService, times(1)).findProductById(productId);
    }
}
