package com.rvega.dreamshops.controller;

import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Cart;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.service.cart.ICartItemService;
import com.rvega.dreamshops.service.cart.ICartService;
import com.rvega.dreamshops.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * This class is responsible for handling HTTP requests related to cart items.
 * It provides endpoints for adding, removing, and updating items in a user's cart.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    /**
     * Adds a new item to the user's cart.
     *
     * @param productId The ID of the product to be added.
     * @param quantity The quantity of the product to be added.
     * @return A ResponseEntity containing an ApiResponse with a success message if the item is added successfully.
     *         If the user is not authenticated, a ResponseEntity with a 401 Unauthorized status and an ApiResponse with an error message.
     *         If the product or cart is not found, a ResponseEntity with a 404 Not Found status and an ApiResponse with an error message.
     */
    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart= cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Removes an item from the user's cart.
     *
     * @param cartId The ID of the cart.
     * @param itemId The ID of the item to be removed.
     * @return A ResponseEntity containing an ApiResponse with a success message if the item is removed successfully.
     *         If the cart or item is not found, a ResponseEntity with a 404 Not Found status and an ApiResponse with an error message.
     */
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Updates the quantity of an item in the user's cart.
     *
     * @param cartId The ID of the cart.
     * @param itemId The ID of the item to be updated.
     * @param quantity The new quantity of the item.
     * @return A ResponseEntity containing an ApiResponse with a success message if the item's quantity is updated successfully.
     *         If the cart or item is not found, a ResponseEntity with a 404 Not Found status and an ApiResponse with an error message.
     */
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public  ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                           @PathVariable Long itemId,
                                                           @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
