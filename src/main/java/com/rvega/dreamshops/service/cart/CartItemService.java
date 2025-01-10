package com.rvega.dreamshops.service.cart;

import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Cart;
import com.rvega.dreamshops.model.CartItem;
import com.rvega.dreamshops.model.Product;
import com.rvega.dreamshops.repository.CartItemRepository;
import com.rvega.dreamshops.repository.CartRepository;
import com.rvega.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    // Dependencies for accessing repositories and services
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    /**
     * Adds an item to the cart. If the item already exists, the quantity is updated.
     * Otherwise, a new CartItem is created and added to the cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product
     * @param quantity  the quantity to add
     */
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // Retrieve the cart and product
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        // Check if the item already exists in the cart
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        if (cartItem.getId() == null) { // New item
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else { // Existing item, update quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // Calculate total price for the item
        cartItem.setTotalPrice();

        // Add the item to the cart and save to the repository
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    /**
     * Removes an item from the cart based on the product ID.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product to remove
     */
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        // Retrieve the cart
        Cart cart = cartService.getCart(cartId);

        // Find and remove the item from the cart
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);

        // Save the updated cart to the repository
        cartRepository.save(cart);
    }

    /**
     * Updates the quantity of a specific item in the cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product to update
     * @param quantity  the new quantity for the product
     */
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // Retrieve the cart
        Cart cart = cartService.getCart(cartId);

        // Find the item and update its quantity
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        // Update the total amount for the cart
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);

        // Save the updated cart to the repository
        cartRepository.save(cart);
    }

    /**
     * Retrieves a specific cart item by cart ID and product ID.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product
     * @return the CartItem if found
     * @throws ResourceNotFoundException if the item is not found
     */
    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        // Retrieve the cart
        Cart cart = cartService.getCart(cartId);

        // Find and return the item, or throw an exception if not found
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
