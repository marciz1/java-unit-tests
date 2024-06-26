package com.matchtrade.javaunittests.domain.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderClient {

  private final WebClient webClient;

  public OrderClient(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  public Mono<Boolean> checkProductAvailability(String productId) {
    return webClient
        .get()
        .uri("/products/{productId}/availability", productId)
        .retrieve()
        .bodyToMono(Boolean.class);
  }
}
