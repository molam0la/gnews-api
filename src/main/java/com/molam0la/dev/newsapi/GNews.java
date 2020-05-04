package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.config.ConfigProps;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GNews {

    private ConfigProps configProps;

    public GNews(ConfigProps configProps) {this.configProps = configProps; }

    public WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(configProps.getBaseUrl())
                .build();
    }
}

