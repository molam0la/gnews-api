package com.molam0la.dev.gnews_api;

import com.molam0la.dev.gnews_api.mappers.GNewsArticleToClientArticleMapper;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import com.molam0la.dev.gnews_api.mappers.GNewsArticleToDBArticleMapper;
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
    private static GNewsArticleService GNewsArticleService;

    @Mock
    private ConfigProps configProps;
    @Mock
    private GNewsArticleToClientArticleMapper gNewsArticleToClientArticleMapper;
    @Mock
    GNewsArticleToDBArticleMapper gNewsArticleToDBArticleMapper;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialise() {
        String baseUrl = mockWebServer.url("/").toString();
        gNews = new GNews(configProps);
        GNewsArticleService = new GNewsArticleService(gNews, configProps, gNewsArticleToClientArticleMapper, gNewsArticleToDBArticleMapper);

        given(configProps.getBaseUrl()).willReturn(baseUrl);
        given(configProps.getLang()).willReturn("en");

    }

    //TODO add expection to GNews - this is not working
    @Test
    void testThrowingTooManyRequestsException() {
        MockResponse mockResponse = new MockResponse();

            mockResponse.setResponseCode(500);
            mockWebServer.enqueue(mockResponse);

            StepVerifier.create(GNewsArticleService.getArticlesByTopic())
                    .expectError(WebClientResponseException.InternalServerError.class).verify();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}