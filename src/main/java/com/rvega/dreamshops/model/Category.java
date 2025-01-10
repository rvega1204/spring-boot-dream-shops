package com.rvega.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a category in the e-commerce application.
 *
 * @author rvega
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    /**
     * Unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category.
     */
    private String name;

    /**
     * List of products associated with this category.
     *
     * <p>This field is annotated with {@link JsonIgnore} to prevent infinite recursion when serializing
     * the category object to JSON. The actual relationship is maintained through the {@link Product#category} field.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    /**
     * Constructor for creating a new category with a given name.
     *
     * @param name Name of the category
     */
    public Category(String name) {
        this.name = name;
    }
}
