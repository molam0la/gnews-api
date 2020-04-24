package com.molam0la.dev.newsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GNews {

    private ConfigProps configProps;

    private static final Logger log = LoggerFactory.getLogger(GNews.class);

    public GNews(ConfigProps configProps) {
        this.configProps = configProps;
    }

    public WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(configProps.getBaseUrl() + "/api/v3/topics/" + configProps.getTopic() + "?token=" + configProps.getApikey())
                .build();
    }

}

