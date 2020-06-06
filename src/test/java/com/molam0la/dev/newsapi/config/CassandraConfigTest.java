package com.molam0la.dev.newsapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CassandraConfig.class)
class CassandraConfigTest {

    @Autowired
    CassandraConfig cassandraConfig;

    @Test
    void getKeyspaceName() { assertEquals("gnews_api", cassandraConfig.getKeyspaceName()); }

    @Test
    void getContactPoints() { assertEquals("127.0.0.1", cassandraConfig.getContactPoints()); }

    @Test
    void getPort() { assertEquals(9042, cassandraConfig.getPort()); }
}