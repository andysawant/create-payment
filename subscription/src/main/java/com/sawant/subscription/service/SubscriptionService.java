package com.sawant.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
public class SubscriptionService {

    public Mono<ServerResponse> createSubscription(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromValue("emp"));
    }
}
