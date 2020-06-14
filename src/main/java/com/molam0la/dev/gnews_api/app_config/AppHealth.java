package com.molam0la.dev.gnews_api.app_config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class AppHealth implements HealthIndicator {

    @Override
    public org.springframework.boot.actuate.health.Health health() {

        //check if app has available memory
        boolean invalid = Runtime.getRuntime().maxMemory() < (100 * 1024 * 1024);
        Status status = invalid ? Status.DOWN : Status.UP;

        return Health.status(status).build();
    }
}
