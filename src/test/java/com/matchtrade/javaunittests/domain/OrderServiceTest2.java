package com.matchtrade.javaunittests.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.matchtrade.javaunittests.domain.model.Order;
import com.matchtrade.javaunittests.domain.model.OrderStatus;
import java.util.Collections;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  private final EasyRandom easyRandom = new EasyRandom();
  @Mock private OrderRepository orderRepository;
  @InjectMocks private OrderService orderService;
  @Captor private ArgumentCaptor<Order> orderCaptor;

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"CANCELED"},
      mode = EnumSource.Mode.EXCLUDE)
  @DisplayName("update status")
  public void shouldUpdateOrderStatusExceptCancelledStatus(OrderStatus status) {
    // given
    Order order = easyRandom.nextObject(Order.class);
    order.setStatus(status);

    given(orderRepository.findById("id")).willReturn(Optional.of(order));

    // when
    orderService.updateOrderStatus("id", status);

    // then
    InOrder inOrder = Mockito.inOrder(orderRepository);

    then(orderRepository).should(inOrder).findById("id");
    then(orderRepository).should(inOrder).save(orderCaptor.capture());
    assertThat(orderCaptor.getValue().getStatus()).isEqualTo(status);
  }

  @Test
  void testSpy() {
    // given
    orderService = spy(new OrderService(orderRepository));
    willDoNothing().given(orderService).throwException();

    // when
    assertThatCode(() -> orderService.findById("id")).doesNotThrowAnyException();
  }

  @Nested
  @DisplayName("Create order tests")
  class CreateOrderTests {
    @Test
    public void shouldCreateOrderWithCreatedStatus() {
      // given
      Order order = easyRandom.nextObject(Order.class);
      given(orderRepository.save(any(Order.class))).willReturn(order);
      // when
      Order result = orderService.createOrder(order);

      // then
      assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    public void shouldThrowExceptionWhenCreateOrderWithoutProducts() {
      // given
      Order order = easyRandom.nextObject(Order.class);
      order.setProductIds(Collections.emptyList());

      // then
      assertThatThrownBy(() -> orderService.createOrder(order))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Order must contain at least one product");
    }
  }
}
