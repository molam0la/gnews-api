package com.molam0la.dev.newsapi;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GNewsTest {

    private static GNews gNews;
    private static MockWebServer mockWebServer;

    @Mock
    private ConfigProps configProps;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialise() {
        String baseUrl = mockWebServer.url("/").toString();
        gNews = new GNews(configProps);

        given(configProps.getBaseUrl()).willReturn(baseUrl);
    }

    //TODO is this test valid?
    @Test
    void testWebClientReturnsANonEmptyResponse() {
        assertThat(gNews.createWebClient().equals(mockWebServer));
    }

    //Add exceptions to Gnews
    @Test
    void testThrowingTooManyRequestsException() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(429);

        given(gNews.createWebClient()).willThrow(IOException.class);

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}