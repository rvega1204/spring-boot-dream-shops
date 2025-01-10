package com.rvega.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    // Primary key for the CartItem entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quantity of the product in the cart item
    private int quantity;

    // Unit price of the product
    private BigDecimal unitPrice;

    // Total price for this cart item (quantity * unit price)
    private BigDecimal totalPrice;

    // Relationship with the Product entity
    @ManyToOne
    @JoinColumn(name = "product_id") // Foreign key column for the product
    private Product product;

    // Relationship with the Cart entity
    @JsonIgnore // Exclude this field from JSON serialization to avoid circular references
    @ManyToOne(cascade = CascadeType.ALL) // Cascade operations to the cart
    @JoinColumn(name = "cart_id") // Foreign key column for the cart
    private Cart cart;

    /**
     * Calculates and sets the total price of the cart item
     * based on the unit price and quantity.
     */
    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
