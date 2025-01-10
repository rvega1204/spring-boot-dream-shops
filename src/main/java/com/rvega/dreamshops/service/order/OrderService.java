package com.rvega.dreamshops.service.order;

import com.rvega.dreamshops.dto.OrderDto;
import com.rvega.dreamshops.enums.OrderStatus;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Cart;
import com.rvega.dreamshops.model.Order;
import com.rvega.dreamshops.model.OrderItem;
import com.rvega.dreamshops.model.Product;
import com.rvega.dreamshops.repository.OrderRepository;
import com.rvega.dreamshops.repository.ProductRepository;
import com.rvega.dreamshops.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    /**
     * Repository for managing Order entities.
     */
    private final OrderRepository orderRepository;

    /**
     * Repository for managing Product entities.
     */
    private final ProductRepository productRepository;

    /**
     * Service for handling cart operations.
     */
    private final CartService cartService;

    /**
     * Mapper for converting entities to DTOs and vice versa.
     */
    private final ModelMapper modelMapper;

    /**
     * Places an order for a user based on their cart contents.
     * Clears the cart after the order is placed.
     *
     * @param userId The ID of the user placing the order.
     * @return The saved Order entity.
     */
    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    /**
     * Creates a new Order entity based on the provided Cart.
     *
     * @param cart The cart from which the order is created.
     * @return The initialized Order entity.
     */
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    /**
     * Creates a list of OrderItem entities based on the Cart and associates them with the given Order.
     * Updates product inventory for each item in the cart.
     *
     * @param order The order to which the items belong.
     * @param cart  The cart containing the items.
     * @return A list of created OrderItem entities.
     */
    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        // Stream through the cart items, transform each into an OrderItem,
        // and return the resulting list.
        return cart.getItems().stream().map(cartItem -> {
            // Retrieve the product associated with the cart item.
            Product product = cartItem.getProduct();

            // Update the product's inventory by subtracting the quantity of the cart item.
            product.setInventory(product.getInventory() - cartItem.getQuantity());

            // Save the updated product to persist the inventory change.
            productRepository.save(product);

            // Create and return a new OrderItem linked to the order and product,
            // with the cart item's quantity and unit price.
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList(); // Collect the transformed stream into a list.
    }

    /**
     * Calculates the total amount for an order based on its items.
     *
     * @param orderItemList The list of items in the order.
     * @return The total amount as a BigDecimal.
     */
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        // Stream through the list of order items, calculate the total for each item (price * quantity),
        // and then sum up the totals using BigDecimal's add method.
        return orderItemList.stream()
                .map(item -> item.getPrice() // Get the price of the current item.
                        .multiply(new BigDecimal(item.getQuantity()))) // Multiply by its quantity.
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Accumulate the totals starting from BigDecimal.ZERO.
    }

    /**
     * Retrieves an order by its ID and converts it to a DTO.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The OrderDto representation of the order.
     * @throws ResourceNotFoundException If the order does not exist.
     */
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    /**
     * Retrieves all orders for a specific user and converts them to DTOs.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of OrderDto objects.
     */
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    /**
     * Converts an Order entity to its DTO representation.
     *
     * @param order The Order entity to convert.
     * @return The OrderDto representation of the order.
     */
    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
