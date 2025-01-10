package com.rvega.dreamshops.enums;

/**
 * Enum representing the various states an order can be in during its lifecycle.
 */
public enum OrderStatus {

    /**
     * The order has been created but not yet processed.
     */
    PENDING,

    /**
     * The order is currently being prepared or handled for processing.
     */
    PROCESSING,

    /**
     * The order has been shipped and is on its way to the destination.
     */
    SHIPPED,

    /**
     * The order has been delivered to the customer successfully.
     */
    DELIVERED,

    /**
     * The order has been canceled and will not be processed or delivered.
     */
    CANCELLED
}
