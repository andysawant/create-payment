package com.sawant.subscription.config;

import com.sawant.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Slf4j
@Configuration
public class SubsriptionRoutingConfig {

    @Value("${welcome.url}")
    private String welcomeUrl;

    @Value("${create.subscription.url}")
    private String createSubscriptionUrl;

    @Bean
    public RouterFunction<ServerResponse> welcome(SubscriptionService subscriptionService){
        return RouterFunctions.route(GET(welcomeUrl), request ->{
            return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromValue("Welcome"));
        });
    }

    @Bean
    public RouterFunction<ServerResponse> addEmployee(SubscriptionService subscriptionService){
        return RouterFunctions.route(POST(createSubscriptionUrl).and(accept(MediaType.APPLICATION_JSON)),
                subscriptionService::createSubscription);
    }

}
