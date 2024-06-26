package com.matchtrade.javaunittests.domain;

import com.matchtrade.javaunittests.domain.domain.OrderService;
import com.matchtrade.javaunittests.domain.domain.model.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    ResponseCookie cookie =
        ResponseCookie.from("exampleCookie", "exampleValue").httpOnly(true).path("/").build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(order);
  }
}
