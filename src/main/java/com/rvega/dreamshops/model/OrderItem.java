package com.rvega.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {

    /**
     * Unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Quantity of the product in this order item.
     */
    private int quantity;

    /**
     * Price of the product for this order item.
     * This may represent the price per unit or the total price based on quantity.
     */
    private BigDecimal price;

    /**
     * The order to which this item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * The product associated with this order item.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Constructor for creating an OrderItem with specified order, product, quantity, and price.
     *
     * @param order    The order to which this item belongs.
     * @param product  The product associated with this item.
     * @param quantity The quantity of the product in this order item.
     * @param price    The price of the product for this order item.
     */
    public OrderItem(Order order, Product product, int quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
