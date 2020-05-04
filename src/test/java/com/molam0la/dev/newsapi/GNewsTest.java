package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.config.ConfigProps;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GNewsTest {

    private static GNews gNews;
    private static MockWebServer mockWebServer;
    private static ArticleService articleService;

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
        articleService = new ArticleService(gNews, configProps);

        given(configProps.getBaseUrl()).willReturn(baseUrl);
    }

    //TODO add expection to GNews - this is not working
    @Test
    void testThrowingTooManyRequestsException() {
        MockResponse mockResponse = new MockResponse();

            mockResponse.setResponseCode(500);
            mockWebServer.enqueue(mockResponse);

            StepVerifier.create(articleService.getArticlesByTopic())
                    .expectError(WebClientResponseException.InternalServerError.class).verify();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}