package com.matchtrade.javaunittests.domain;

import com.matchtrade.javaunittests.domain.model.Order;
import com.matchtrade.javaunittests.domain.model.OrderStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public Order createOrder(Order order) {
    if (order.getProductIds().isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one product");
    }
    order.setStatus(OrderStatus.CREATED);
    return orderRepository.save(order);
  }

  public void updateOrderStatus(String orderId, OrderStatus status) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

    if (order.getStatus() == OrderStatus.CANCELED) {
      throw new IllegalStateException("Cannot update status of a canceled order");
    }

    order.setStatus(status);
    orderRepository.save(order);
  }

  public Optional<Order> findById(String id) {
    throwException();
    return orderRepository.findById(id);
  }

  public void throwException() {
    throw new RuntimeException();
  }
}
