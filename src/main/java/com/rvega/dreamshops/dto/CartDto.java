package com.rvega.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Data Transfer Object for Cart.
 */
@Data
public class CartDto {
    /**
     * The unique identifier for the cart.
     */
    private Long cartId;

    /**
     * The set of items in the cart.
     */
    private Set<CartItemDto> items;

    /**
     * The total amount for the cart.
     */
    private BigDecimal totalAmount;
}
