package com.molam0la.dev.newsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class GNews {

    private final String APIKEY = "e7e97729496805263f48e6cd347f39cc";
    private static final Logger log = LoggerFactory.getLogger(GNews.class);
    private WebClient.Builder webClientBuilder;

    @Autowired
    public GNews(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public WebClient createWebClient() {

        return webClientBuilder
                .baseUrl("https://gnews.io/api/v3/topics/technology?token=" + APIKEY)
                .build();
    }

}

