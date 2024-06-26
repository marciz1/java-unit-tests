package com.matchtrade.javaunittests;

import com.matchtrade.javaunittests.domain.OrderService;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

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
                Optional.of(new Order("1", "user123", List.of("product1", "product2"), "CREATED"))
        );
    }
}
