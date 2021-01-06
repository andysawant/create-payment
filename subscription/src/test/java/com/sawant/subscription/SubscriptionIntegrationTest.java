package com.sawant.subscription;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { SubscriptionApplication.class })
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class SubscriptionIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient testClient;

    @Value("${create.subscription.url}")
    private String createSubscriptionUrl;

    @Before
    public void setup() throws IOException {
        testClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @After
    public void tearDown() throws IOException {

    }

    @Test
    public void end2EndIntegratedTests() throws Exception {
        EntityExchangeResult<String> response = testClient.mutate().responseTimeout(Duration.ofMinutes(10)).build().post()
                .uri(uriBuilder -> uriBuilder.path(createSubscriptionUrl).build()).header("Content-Type", "application/json")
                .body(Mono.just(new String("Test")), String.class).exchange().expectStatus().is2xxSuccessful().expectBody(String.class).returnResult();

        String successResponse = response.getResponseBody();
        Assert.assertTrue("Should have order-id in response.", successResponse.contains("orderId"));
    }

}
