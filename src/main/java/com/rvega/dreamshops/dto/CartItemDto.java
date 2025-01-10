package com.rvega.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing an item in a shopping cart.
 */
@Data
public class CartItemDto {
    /**
     * The unique identifier of the item.
     */
    private Long itemId;

    /**
     * The quantity of the item in the cart.
     */
    private Integer quantity;

    /**
     * The unit price of the item.
     */
    private BigDecimal unitPrice;

    /**
     * The product details associated with the item.
     */
    private ProductDto product;
}
