package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.Wishlist;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private WishlistItemService wishlistItemService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WishlistController wishlistController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserWishlist() throws UserException {
        // Given
        String jwt = "Bearer valid-token";
        User user = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
        Wishlist wishlist = new Wishlist(1L, user, null, 0);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(wishlistService.findUserWishlist(user.getUser_id())).thenReturn(wishlist);

        // When
        ResponseEntity<Wishlist> response = wishlistController.findUserWishlist(jwt);

        // Expected and Actual Output
        System.out.println("Expected: " + wishlist);
        System.out.println("Actual: " + response.getBody());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(wishlist.getWishlist_id(), response.getBody().getWishlist_id());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistService, times(1)).findUserWishlist(user.getUser_id());
    }

    @Test
    public void testAddItemToWishlist() throws UserException, ProductException {
        // Given
        String jwt = "Bearer valid-token";
        AddItemRequest req = new AddItemRequest(1L, "M", 0, null);
        User user = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);
        Product product = new Product(1L, "Dress", "Clothing", 999, 899, 10, 5, "BrandX", "Black", 
                new HashSet<>(), "image_url", new ArrayList<>(), new ArrayList<>(), 5, new Category(), 
                LocalDateTime.now());
        Wishlist wishlist = new Wishlist(1L, user, null, 0);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);
        when(wishlistService.findUserWishlist(user.getUser_id())).thenReturn(wishlist);
        when(productService.findProductById(req.getProductId())).thenReturn(product);

        // When
        ResponseEntity<ApiResponse> response = wishlistController.addItemToWishlist(req, jwt);

        // Expected and Actual Output
        System.out.println("Expected: Item Added to Wishlist Successfully");
        System.out.println("Actual: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("Item Added to Wishlist Successfully", response.getBody().getMsg());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistService, times(1)).addWishlistItem(user.getUser_id(), req, product.getProduct_id());
    }

    @Test
    public void testAddProductToCart() throws UserException, ProductException {
        // Given
        String jwt = "Bearer valid-token";
        AddItemRequest req = new AddItemRequest(1L, "L", 0, null);
        User user = new User(1L, "John Doe", "johndoe@example.com", "password", jwt, null, null, null, null, null, null, null);

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(user);

        // When
        ResponseEntity<ApiResponse> response = wishlistController.addProductToCart(req, jwt);

        // Expected and Actual Output
        System.out.println("Expected: Item Added to Cart Successfully");
        System.out.println("Actual: " + response.getBody().getMsg());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("Item Added to Cart Successfully", response.getBody().getMsg());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(cartService, times(1)).addCartItem(user.getUser_id(), req);
    }
}
