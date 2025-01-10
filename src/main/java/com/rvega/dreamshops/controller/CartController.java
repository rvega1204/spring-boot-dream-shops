package com.rvega.dreamshops.controller;

import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Cart;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    // Service dependency to manage cart operations
    private final ICartService cartService;

    /**
     * Retrieves a cart by its ID.
     *
     * @param cartId the ID of the cart to retrieve
     * @return ResponseEntity with the cart data if found, or NOT_FOUND status if the cart doesn't exist
     */
    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            // Retrieve the cart from the service
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            // Return NOT_FOUND status if the cart is not found
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Clears all items in a cart by its ID.
     *
     * @param cartId the ID of the cart to clear
     * @return ResponseEntity indicating success or NOT_FOUND status if the cart doesn't exist
     */
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            // Clear the cart using the service
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (ResourceNotFoundException e) {
            // Return NOT_FOUND status if the cart is not found
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Retrieves the total price of all items in a cart by its ID.
     *
     * @param cartId the ID of the cart
     * @return ResponseEntity with the total price of the cart if found, or NOT_FOUND status if the cart doesn't exist
     */
    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            // Retrieve the total price of the cart
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            // Return NOT_FOUND status if the cart is not found
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
