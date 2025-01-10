package com.rvega.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    // ID of the cart, auto-generated with a strategy of identity.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Total amount of the cart, initialized to zero.
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Set of CartItems in the cart. Cascade operations propagate to CartItem entities.
    // Orphan removal ensures CartItem is removed when not referenced by any Cart.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    // User who owns the cart. This is a one-to-one relationship with the User entity.
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    /**
     * Adds a CartItem to the cart, setting the cart reference in the CartItem and updating the total amount.
     *
     * @param item the CartItem to add
     */
    public void addItem(CartItem item) {
        this.items.add(item);  // Adds the item to the cart's item list
        item.setCart(this);  // Sets the current cart reference in the item
        updateTotalAmount();  // Recalculates the total amount of the cart after adding the item
    }

    /**
     * Removes a CartItem from the cart, setting the cart reference in the CartItem to null and updating the total amount.
     *
     * @param item the CartItem to remove
     */
    public void removeItem(CartItem item) {
        this.items.remove(item);  // Removes the item from the cart's item list
        item.setCart(null);  // Sets the cart reference to null in the removed item
        updateTotalAmount();  // Recalculates the total amount of the cart after removing the item
    }

    /**
     * Updates the total amount of the cart by summing the price of all items.
     * Each item's price is multiplied by its quantity, and the result is added to the total amount.
     */
    private void updateTotalAmount() {
        this.totalAmount = items.stream()  // Streams through each CartItem in the cart
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();  // Gets the unit price of the item
                    if (unitPrice == null) {  // If the unit price is null, assume a zero price
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));  // Multiply price by quantity
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // Adds up the total amount of all items in the cart
    }
}
