/**
 * Data Transfer Object (DTO) for representing an order.
 * This class is used to transfer order data between different layers of the application.
 * It includes information about the order such as the order ID, user ID, order date, total amount, status, and the list of order items.
 * 
 * Annotations:
 * - @Data: A Lombok annotation to generate boilerplate code such as getters, setters, toString, equals, and hashCode methods.
 * 
 * Fields:
 * - id: The unique identifier of the order.
 * - userId: The unique identifier of the user who placed the order.
 * - orderDate: The date and time when the order was placed.
 * - totalAmount: The total amount of the order.
 * - status: The current status of the order (e.g., pending, completed, cancelled).
 * - items: The list of items included in the order.
 */
package com.rvega.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDto> items;
}
