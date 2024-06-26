package com.matchtrade.javaunittests.domain;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.matchtrade.javaunittests.domain.model.Order;
import com.matchtrade.javaunittests.domain.model.OrderStatus;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public abstract class ContractVerifierBase {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        RestAssuredWebTestClient.webTestClient(this.webTestClient);
        given(orderService.findById(anyString())).willReturn(
                Optional.of(new Order("1", "userId", List.of("123", "12345"), OrderStatus.CREATED))
        );
    }
}
