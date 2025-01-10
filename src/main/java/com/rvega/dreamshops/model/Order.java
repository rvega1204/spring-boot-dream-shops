package com.rvega.dreamshops.model;

import com.rvega.dreamshops.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Unique identifier for each order, generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /**
     * The date when the order was placed.
     */
    private LocalDate orderDate;

    /**
     * The total monetary amount of the order, calculated as the sum of all associated order items.
     */
    private BigDecimal totalAmount;

    /**
     * The current status of the order, represented as an enumeration (e.g., PENDING, COMPLETED, CANCELED).
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * A collection of items included in the order.
     * This relationship is one-to-many, with the order as the parent entity.
     * Cascade operations ensure that changes to the order are reflected in its items.
     * Orphan removal ensures that items removed from the set are deleted from the database.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    /**
     * The user who placed the order.
     * This is a many-to-one relationship, as a user can place multiple orders.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
