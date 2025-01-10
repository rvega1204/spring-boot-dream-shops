package com.rvega.dreamshops.service.product;

import com.rvega.dreamshops.dto.ImageDto;
import com.rvega.dreamshops.dto.ProductDto;
import com.rvega.dreamshops.exceptions.AlreadyExistsException;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Category;
import com.rvega.dreamshops.model.Image;
import com.rvega.dreamshops.model.Product;
import com.rvega.dreamshops.repository.CategoryRepository;
import com.rvega.dreamshops.repository.ImageRepository;
import com.rvega.dreamshops.repository.ProductRepository;
import com.rvega.dreamshops.request.AddProductRequest;
import com.rvega.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    /**
     * Adds a new product to the database. If the product already exists, an exception is thrown.
     *
     * @param request The request object containing the product details.
     * @return The saved product.
     * @throws AlreadyExistsException If the product with the same name and brand already exists.
     */
    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExists(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException(request.getName() + " " + request.getBrand() + " already exists, you may update it!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    /**
     * Creates a new Product object using the provided request and category.
     *
     * @param request  The request object containing the product details.
     * @param category The category to which the product belongs.
     * @return The newly created Product object.
     */
    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }


    /**
     * Retrieves a product from the database by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return The product with the specified identifier. If no product is found, a {@link ResourceNotFoundException} is thrown.
     * @throws ResourceNotFoundException If no product is found with the specified identifier.
     */
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    /**
     * Deletes a product from the database by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     * @throws ResourceNotFoundException If no product is found with the specified identifier.
     */
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Product not found!");
                        });
    }

    /**
     * Updates an existing product in the database based on the provided request and product identifier.
     *
     * @param request   The request object containing the updated product details.
     * @param productId The unique identifier of the product to update.
     * @return The updated product. If no product is found with the specified identifier, a {@link ResourceNotFoundException} is thrown.
     * @throws ResourceNotFoundException If no product is found with the specified identifier.
     */
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    /**
     * Updates the existing product with the details from the provided request.
     *
     * @param existingProduct The existing product to be updated.
     * @param request         The request object containing the updated product details.
     * @return The updated product.
     */
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products in the database. If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a list of products from the database that belong to a specific category.
     *
     * @param category The name of the category to filter products by.
     * @return A list of products that belong to the specified category. If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    /**
     * Retrieves a list of products from the database that belong to a specific brand.
     *
     * @param brand The name of the brand to filter products by.
     * @return A list of products that belong to the specified brand. If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    /**
     * Retrieves a list of products from the database that belong to a specific category and brand.
     *
     * @param category The name of the category to filter products by. This parameter cannot be null or empty.
     * @param brand    The name of the brand to filter products by. This parameter cannot be null or empty.
     * @return A list of products that belong to the specified category and brand.
     * If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    /**
     * Retrieves a list of products from the database that match the specified name.
     *
     * @param name The name of the product to filter products by. This parameter cannot be null or empty.
     * @return A list of products that match the specified name. If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    /**
     * Retrieves a list of products from the database that match the specified brand and name.
     *
     * @param brand The name of the brand to filter products by. This parameter cannot be null or empty.
     * @param name  The name of the product to filter products by. This parameter cannot be null or empty.
     * @return A list of products that match the specified brand and name.
     * If no products are found, an empty list is returned.
     */
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    /**
     * Retrieves the count of products from the database that match the specified brand and name.
     *
     * @param brand The name of the brand to filter products by.
     * @param name  The name of the product to filter products by.
     * @return The count of products that match the specified brand and name.
     * If no products are found, the method returns 0.
     */
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    /**
     * Converts a list of Product entities to a list of ProductDto objects, including associated images.
     *
     * @param products The list of Product entities to be converted.
     * @return A list of converted ProductDto objects. Each ProductDto object contains the details of a Product entity,
     * including associated images.
     */
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    /**
     * Converts a Product entity to a ProductDto object, including associated images.
     *
     * @param product The Product entity to be converted.
     * @return The converted ProductDto object.
     */
    @Override
    public ProductDto convertToDto(Product product) {
        // Use ModelMapper to map the Product entity to a ProductDto object
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        // Retrieve associated images from the database using the product's ID
        List<Image> images = imageRepository.findByProductId(product.getId());

        // Convert each Image entity to an ImageDto object and store them in a list
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();

        // Set the list of ImageDto objects in the ProductDto object
        productDto.setImages(imageDtos);

        // Return the converted ProductDto object
        return productDto;
    }
}
