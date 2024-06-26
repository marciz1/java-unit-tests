package com.matchtrade.javaunittests.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.matchtrade.javaunittests.domain.model.Order;
import com.matchtrade.javaunittests.domain.model.OrderStatus;
import java.util.Collections;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock OrderRepository orderRepository;
  @InjectMocks OrderService underTest;
  @Captor ArgumentCaptor<Order> orderCaptor;
  private EasyRandom easyRandom = new EasyRandom();

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"CANCELED"},
      mode = EnumSource.Mode.EXCLUDE)
  void shouldUpdateOrderWithStatus(OrderStatus status) {
    // given
    Order order = easyRandom.nextObject(Order.class);
    order.setStatus(status);

    String id = "id";
    given(orderRepository.save(orderCaptor.capture())).willReturn(order);
    given(orderRepository.findById(id)).willReturn(Optional.of(order));

    // when
    underTest.updateOrderStatus(id, status);

    // then
    SoftAssertions softly = new SoftAssertions();

    softly.assertThat(orderCaptor.getValue().getStatus()).isEqualTo(status);
    softly.assertThat(orderCaptor.getValue().getStatus()).isNotEqualTo(status);
    softly.assertThat(orderCaptor.getValue().getStatus()).isNotEqualTo(status);

    softly.assertAll();

    InOrder inOrder = Mockito.inOrder(orderRepository);

    then(orderRepository).should(inOrder).findById(id);
    then(orderRepository).should(inOrder).save(order);
  }

  @Nested
  @DisplayName("create order")
  class CreateOrder {
    @Disabled
    @Test
    void shouldCreateOrderWithCreatedStatus() {
      // given
      Order order = easyRandom.nextObject(Order.class);
      given(orderRepository.save(any())).willReturn(order);

      // when
      Order result = underTest.createOrder(order);

      // then
      assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    void shouldThrowExceptionWhenProductListIsEmpty() {
      // given
      Order order = easyRandom.nextObject(Order.class);
      order.setProductIds(Collections.emptyList());

      // then
      assertThatThrownBy(() -> underTest.createOrder(order))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Order must contain at least one product");
    }

    @Test
    void testSpy() {
      // when
      OrderService spy = Mockito.spy(new OrderService(orderRepository));
      willDoNothing().given(spy).throwException();

      // then
      assertThatCode(() -> spy.findById("id")).doesNotThrowAnyException();
    }
  }
}
