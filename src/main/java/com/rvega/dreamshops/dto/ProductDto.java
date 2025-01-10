package com.rvega.dreamshops.dto;

import com.rvega.dreamshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a Data Transfer Object for a product in the e-commerce application.
 * This class is used to transfer product data between processes.
 */
@Data
public class ProductDto {
    /**
     * The unique identifier of the product.
     */
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The brand associated with the product.
     */
    private String brand;

    /**
     * The price of the product.
     */
    private BigDecimal price;

    /**
     * The number of items available in inventory.
     */
    private int inventory;

    /**
     * A brief description of the product.
     */
    private String description;

    /**
     * The category to which the product belongs.
     */
    private Category category;

    /**
     * A list of images associated with the product.
     */
    private List<ImageDto> images;
}
