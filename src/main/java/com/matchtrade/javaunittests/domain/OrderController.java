package com.matchtrade.javaunittests.domain;

import com.matchtrade.javaunittests.domain.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable String id) {
    Order order = orderService.findById(id).orElseThrow(RuntimeException::new);
    return ResponseEntity.ok(order);
  }
}
