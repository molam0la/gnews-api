package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.config.ConfigProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConfigProps.class)
@ActiveProfiles("test")
class ConfigPropsTest {

    @Autowired
    private ConfigProps configProps;

    @Test
    void getBaseUrl() { assertEquals("testBaseUrl", configProps.getBaseUrl()); }

    @Test
    void getApikey() { assertEquals("testApikey", configProps.getApikey()); }

    @Test
    void getTopic() { assertEquals("testTopic", configProps.getTopic()); }
}