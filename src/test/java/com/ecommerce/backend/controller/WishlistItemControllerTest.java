package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.backend.exception.UserException;
import com.ecommerce.backend.exception.WishlistItemException;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishlistItem;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishlistItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest
class WishlistItemControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private WishlistItemService wishlistItemService;

    @InjectMocks
    private WishlistItemController wishlistItemController;

    private User mockUser;
    private WishlistItem mockWishlistItem;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User(1L, "John Doe", "johndoe@example.com", "password", "valid-jwt", null, null, null, null, null, null, null);
        mockWishlistItem = new WishlistItem();
        mockWishlistItem.setWishlistItems_id(1L);
        mockWishlistItem.setSize("M");
    }

    @Test
    public void testDeleteWishlistItem_Success() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer valid-token";
        Long wishlistItemId = 1L;

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(mockUser);
        doNothing().when(wishlistItemService).removeWishlistItem(mockUser.getUser_id(), wishlistItemId);

        // When
        ResponseEntity<ApiResponse> response = wishlistItemController.deleteWishlistItem(wishlistItemId, jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("Item Removed From Wishlist", response.getBody().getMsg());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, times(1)).removeWishlistItem(mockUser.getUser_id(), wishlistItemId);
    }

    @Test
    public void testDeleteWishlistItem_InvalidJwt() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer invalid-token";
        Long wishlistItemId = 1L;

        when(userService.findUserProfileByJwtId(jwt)).thenThrow(new UserException("Invalid token"));

        // When
        UserException exception = assertThrows(UserException.class, () -> {
            wishlistItemController.deleteWishlistItem(wishlistItemId, jwt);
        });

        // Then
        assertEquals("Invalid token", exception.getMessage());
        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, never()).removeWishlistItem(anyLong(), anyLong());
    }

    @Test
    public void testDeleteWishlistItem_NonexistentItem() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer valid-token";
        Long wishlistItemId = 99L;

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(mockUser);
        doThrow(new WishlistItemException("Wishlist item not found"))
                .when(wishlistItemService).removeWishlistItem(mockUser.getUser_id(), wishlistItemId);

        // When
        WishlistItemException exception = assertThrows(WishlistItemException.class, () -> {
            wishlistItemController.deleteWishlistItem(wishlistItemId, jwt);
        });

        // Then
        assertEquals("Wishlist item not found", exception.getMessage());
        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, times(1)).removeWishlistItem(mockUser.getUser_id(), wishlistItemId);
    }

    @Test
    public void testUpdateWishlistItem_Success() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer valid-token";
        Long wishlistItemId = 1L;
        WishlistItem updatedWishlistItem = new WishlistItem();
        updatedWishlistItem.setWishlistItems_id(wishlistItemId);
        updatedWishlistItem.setSize("L");

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(mockUser);
        when(wishlistItemService.updateWishlistItem(mockUser.getUser_id(), wishlistItemId, updatedWishlistItem))
                .thenReturn(updatedWishlistItem);

        // When
        ResponseEntity<WishlistItem> response = wishlistItemController.updateWishlistItem(updatedWishlistItem, wishlistItemId, jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedWishlistItem, response.getBody());

        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, times(1)).updateWishlistItem(mockUser.getUser_id(), wishlistItemId, updatedWishlistItem);
    }

    @Test
    public void testUpdateWishlistItem_InvalidJwt() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer invalid-token";
        Long wishlistItemId = 1L;
        WishlistItem updatedWishlistItem = new WishlistItem();

        when(userService.findUserProfileByJwtId(jwt)).thenThrow(new UserException("Invalid token"));

        // When
        UserException exception = assertThrows(UserException.class, () -> {
            wishlistItemController.updateWishlistItem(updatedWishlistItem, wishlistItemId, jwt);
        });

        // Then
        assertEquals("Invalid token", exception.getMessage());
        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, never()).updateWishlistItem(anyLong(), anyLong(), any());
    }

    @Test
    public void testUpdateWishlistItem_NonexistentItem() throws UserException, WishlistItemException {
        // Given
        String jwt = "Bearer valid-token";
        Long wishlistItemId = 99L;
        WishlistItem updatedWishlistItem = new WishlistItem();

        when(userService.findUserProfileByJwtId(jwt)).thenReturn(mockUser);
        when(wishlistItemService.updateWishlistItem(mockUser.getUser_id(), wishlistItemId, updatedWishlistItem))
                .thenThrow(new WishlistItemException("Wishlist item not found"));

        // When
        WishlistItemException exception = assertThrows(WishlistItemException.class, () -> {
            wishlistItemController.updateWishlistItem(updatedWishlistItem, wishlistItemId, jwt);
        });

        // Then
        assertEquals("Wishlist item not found", exception.getMessage());
        verify(userService, times(1)).findUserProfileByJwtId(jwt);
        verify(wishlistItemService, times(1)).updateWishlistItem(mockUser.getUser_id(), wishlistItemId, updatedWishlistItem);
    }
}
