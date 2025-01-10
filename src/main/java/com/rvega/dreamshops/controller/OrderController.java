package com.rvega.dreamshops.controller;

import com.rvega.dreamshops.dto.OrderDto;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Order;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    // Injected IOrderService for business logic related to orders.
    private final IOrderService orderService;

    /**
     * Creates a new order for a user.
     *
     * @param userId the ID of the user placing the order
     * @return a response indicating the success or failure of the order creation
     */
    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            // Places the order for the given user ID and returns the order.
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
        } catch (Exception e) {
            // Catches any exceptions and responds with an error message.
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the order details in a response body
     * @throws ResourceNotFoundException if the order with the given ID is not found
     */
    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            // Retrieves the order by its ID and returns the order details.
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
        } catch (ResourceNotFoundException e) {
            // If the order is not found, responds with a 404 error.
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }

    /**
     * Retrieves all orders for a specific user.
     *
     * @param userId the ID of the user whose orders to retrieve
     * @return a list of orders in a response body
     * @throws ResourceNotFoundException if no orders are found for the user
     */
    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            // Retrieves the user's orders and returns them in the response.
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orders));
        } catch (ResourceNotFoundException e) {
            // If no orders are found, responds with a 404 error.
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }
}
