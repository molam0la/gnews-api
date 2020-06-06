package com.molam0la.dev.newsapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("cassandra")
public class CassandraConfig {

    private String keyspaceName;
    private String contactPoints;
    private int port;

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public String getContactPoints() {
        return contactPoints;
    }

    public int getPort() {
        return port;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public void setContactPoints(String contactPoints) {
        this.contactPoints = contactPoints;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
