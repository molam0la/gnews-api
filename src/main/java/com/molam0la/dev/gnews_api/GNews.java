package com.molam0la.dev.gnews_api;

import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GNews {

    private int requestCount;

    private ConfigProps configProps;

    private static final Logger log = LoggerFactory.getLogger(GNews.class);

    public GNews(ConfigProps configProps) {this.configProps = configProps; }

    public WebClient createWebClient() {
        requestCount+=1;
        log.info("Requests made " + requestCount);

        return WebClient.builder()
                .baseUrl(configProps.getBaseUrl())
                .build();
    }
}

