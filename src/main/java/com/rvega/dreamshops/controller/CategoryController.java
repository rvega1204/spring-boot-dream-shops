package com.rvega.dreamshops.controller;


import com.rvega.dreamshops.exceptions.AlreadyExistsException;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Category;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * Controller for managing categories in the application.
 * Provides endpoints for CRUD operations on categories.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    /**
     * Service layer to handle category-related business logic.
     */
    private final ICategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return ResponseEntity containing an ApiResponse with the list of categories and an HTTP status code.
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Adds a new category.
     *
     * @param category The category to be added.
     * @return ResponseEntity containing an ApiResponse with the added category and an HTTP status code.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category theCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success", theCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to be retrieved.
     * @return ResponseEntity containing an ApiResponse with the category and an HTTP status code.
     */
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category theCategory = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name The name of the category to be retrieved.
     * @return ResponseEntity containing an ApiResponse with the category and an HTTP status code.
     */
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to be deleted.
     * @return ResponseEntity containing an ApiResponse and an HTTP status code.
     */
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Updates a category by its ID.
     *
     * @param id       The ID of the category to be updated.
     * @param category The updated category data.
     * @return ResponseEntity containing an ApiResponse with the updated category and an HTTP status code.
     */
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
