package com.rvega.dreamshops.dto;

import lombok.Data;

import java.util.List;

/**
 * Represents a Data Transfer Object (DTO) for a User.
 * This class is used to transfer user data between processes.
 * It includes user details such as ID, name, email, orders, and cart.
 */
@Data
public class UserDto {
    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * A list of orders associated with the user.
     */
    private List<OrderDto> orders;

    /**
     * The shopping cart associated with the user.
     */
    private CartDto cart;
}
