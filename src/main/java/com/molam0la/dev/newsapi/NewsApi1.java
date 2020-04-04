package com.molam0la.dev.newsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class NewsApi1 {

    public WebClient webClient;

    private final String APIKEY = "e7e97729496805263f48e6cd347f39cc";
    private static final Logger log = LoggerFactory.getLogger(NewsApi1.class);
    private WebClient.Builder webClientBuilder;

    @Autowired
    public NewsApi1(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> makeRequest() {

        webClient = webClientBuilder
                .baseUrl("https://gnews.io/api/v3/topics/world?token=" + APIKEY)
                .build();

        return webClient
                .get()
                .retrieve()
                .bodyToMono(String.class);
    }

}

