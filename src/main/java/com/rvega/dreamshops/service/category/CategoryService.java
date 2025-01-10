package com.rvega.dreamshops.service.category;

import com.rvega.dreamshops.exceptions.AlreadyExistsException;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Category;
import com.rvega.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    
    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The unique identifier of the category.
     * @return The category with the given id.
     * @throws ResourceNotFoundException If no category is found with the given id.
     */
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name The name of the category.
     * @return The category with the given name.
     */
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Retrieves all categories.
     *
     * @return A list of all categories.
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Adds a new category.
     *
     * @param category The category to be added.
     * @return The added category.
     * @throws AlreadyExistsException If a category with the same name already exists.
     */
    @Override
    public Category addCategory(Category category) {
        return  Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    /**
     * Updates an existing category.
     *
     * @param category The updated category.
     * @param id The unique identifier of the category to be updated.
     * @return The updated category.
     * @throws ResourceNotFoundException If no category is found with the given id.
     */
    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }) .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    /**
     * Deletes a category by its unique identifier.
     *
     * @param id The unique identifier of the category to be deleted.
     * @throws ResourceNotFoundException If no category is found with the given id.
     */
    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
