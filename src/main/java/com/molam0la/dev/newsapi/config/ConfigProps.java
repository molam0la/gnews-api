package com.molam0la.dev.newsapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("gnews")
public class ConfigProps {

    private String baseUrl;
    private String apikey;
    private String topic;

    public String getBaseUrl() { return baseUrl; }

    public String getApikey() { return apikey; }

    public String getTopic() { return topic; }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
