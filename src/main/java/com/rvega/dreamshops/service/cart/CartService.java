package com.rvega.dreamshops.service.cart;

import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Cart;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.repository.CartItemRepository;
import com.rvega.dreamshops.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    // Repository for accessing cart data
    private final CartRepository cartRepository;

    // Repository for accessing cart item data
    private final CartItemRepository cartItemRepository;

    // Atomic generator for unique cart IDs
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    /**
     * Retrieves a cart by its ID. If the cart is not found, throws a ResourceNotFoundException.
     * Also updates and persists the total amount of the cart.
     *
     * @param id The ID of the cart to retrieve.
     * @return The retrieved cart.
     */
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    /**
     * Clears all items from the cart and removes the cart itself from the database.
     * This method is transactional to ensure data consistency.
     *
     * @param id The ID of the cart to clear.
     */
    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id); // Delete all cart items associated with this cart
        cart.getItems().clear(); // Clear the cart's item list
        cartRepository.deleteById(id); // Delete the cart
    }

    /**
     * Retrieves the total price of the cart by its ID.
     *
     * @param id The ID of the cart.
     * @return The total price of the cart.
     */
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    /**
     * Initializes a new cart with a unique ID and saves it to the database.
     *
     * @return The ID of the newly created cart.
     */
    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()-> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    /**
     * Retrieves a cart associated with a specific user ID.
     * Assumes that the cartRepository has a method to find carts by user ID.
     *
     * @param userId The ID of the user.
     * @return The cart associated with the user.
     */
    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId); // Fetch cart by user ID
    }
}
