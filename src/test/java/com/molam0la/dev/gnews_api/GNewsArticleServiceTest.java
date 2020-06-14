package com.molam0la.dev.gnews_api;

import com.molam0la.dev.gnews_api.mappers.GNewsArticleToClientArticleMapper;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GNewsArticleServiceTest {

    private GNewsArticleService GNewsArticleService;
    private GNewsArticleToClientArticleMapper gnewsArticleToClientArticleMapper;
    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    private String ARTICLE_STUB;

    @Mock
    private GNews gNews;
    @Mock
    private ConfigProps configProps;

    @BeforeAll
    static void initialiseWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setUp() throws IOException {

        //set webclient
        String baseUrl = mockWebServer.url("/").toString();
        given(configProps.getBaseUrl()).willReturn(baseUrl);

        gNews = new GNews(configProps);
        gnewsArticleToClientArticleMapper = new GNewsArticleToClientArticleMapper();
        GNewsArticleService = new GNewsArticleService(gNews, configProps, gnewsArticleToClientArticleMapper);
        given(configProps.getLang()).willReturn("en");

        //set stub article body
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/articleMock.json"));
        ARTICLE_STUB = bufferedReader.readLine();

        //set mockresponse
        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody(ARTICLE_STUB);

    }

    @Test
    void getArticlesByTopic_returnsCorrectArticleCount() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(GNewsArticleService.getArticlesByTopic())
                .expectNextMatches(articleInputModel -> articleInputModel.getArticleCount() == 10)
                .verifyComplete();
    }

    @Test
    void getArticlesBySearchWord_returnsCorrectTimestamp() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(GNewsArticleService.getArticlesBySearchWord())
                .expectNextMatches(articleInputModel -> articleInputModel.getTimestamp() == 1585339920)
                .verifyComplete();

    }

    @Test
    void getArticlesBySearchWord_returnsCorrectSourceName() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        assertTrue(GNewsArticleService
                .createListOfArticles(GNewsArticleService.getArticlesBySearchWord())
                .block()
                .get(0)
                .getSourceName()
                .equals("The Wall Street Journal"));
    }

    @Test
    void getArticlesByTopic_returns4xxError() {
        mockResponse.setResponseCode(404);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(GNewsArticleService.createListOfArticles(GNewsArticleService.getArticlesByTopic()))
                .expectError(NotFound.class).verify();
    }

    @Test
    void getArticlesByTopic_returns5xxError() {
        mockResponse.setResponseCode(500);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(GNewsArticleService.getArticlesByTopic())
                .expectError(InternalServerError.class).verify();
    }

    @Test
    void getArticlesByTopic_throwsTooManyRequestsException() {
        mockResponse.setResponseCode(429);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(GNewsArticleService.getArticlesByTopic())
                .expectError(TooManyRequests.class).verify();
    }



    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}