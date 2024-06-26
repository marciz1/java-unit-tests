package com.matchtrade.javaunittests.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.matchtrade.javaunittests.domain.OrderClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderClientMockServerTest {

  private static ClientAndServer mockServer;
  @Autowired private OrderClient orderClient;

  @BeforeAll
  public static void startServer() {
    mockServer = ClientAndServer.startClientAndServer(1080);
  }

  @AfterAll
  public static void stopServer() {
    mockServer.stop();
  }

  @BeforeEach
  public void resetServer() {
    mockServer.reset();
  }

  @Test
  void shouldReturnTrueWhenProductAvailable() {
    // given
    String productId = "product1";
    mockServer
        .when(request().withMethod("GET").withPath("/products/product1/availability"))
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("true"));

    // when
    Mono<Boolean> availability = orderClient.checkProductAvailability(productId);

    // then
    assertThat(availability.block()).isTrue();
  }

  @Configuration
  static class TestConfig {

    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
      return WebClient.builder().baseUrl("http://localhost:1080");
    }

    @Bean
    public OrderClient orderClient(WebClient.Builder webClientBuilder) {
      return new OrderClient(webClientBuilder);
    }
  }
}
