/**
 * Data Transfer Object (DTO) for Order Item.
 * This class is used to transfer order item data between processes.
 * It includes details about the product, such as its ID, name, quantity, and price.
 * 
 * Annotations:
 * @Data - Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 * 
 * Fields:
 * @param productId   the unique identifier of the product
 * @param productName the name of the product
 * @param productBrand the brand of the product
 * @param quantity    the quantity of the product ordered
 * @param price       the price of the product
 */
package com.rvega.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
