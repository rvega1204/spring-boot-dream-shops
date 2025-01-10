package com.rvega.dreamshops.service.order;

import com.rvega.dreamshops.dto.OrderDto;
import com.rvega.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
