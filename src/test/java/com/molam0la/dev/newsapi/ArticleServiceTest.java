package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private GNews gNews;
    private ArticleService articleService;
    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    private String MOCK_ARTICLE;
    private Mono<ArticleInput> MOCK_ARTICLE_INPUT;

    @Mock
    ConfigProps configProps;

    @BeforeAll
    static void initialiseWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        gNews = new GNews(configProps);
        articleService = new ArticleService(gNews);

        //set webclient call
        String baseUrl = mockWebServer.url("/").toString();
        given(configProps.getBaseUrl()).willReturn(baseUrl);

        //set mockarticle body
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/sandra/Documents/Dev/news-api/src/test/resources/articleMock.json"));
        MOCK_ARTICLE = bufferedReader.readLine();

        //set mockresponse
        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
    }

    @Test
    void testGettingResponseFromWebClientWithCorrectArticleCount() {
        mockResponse.setResponseCode(200);
        mockResponse.setBody(MOCK_ARTICLE);
        mockWebServer.enqueue(mockResponse);

        MOCK_ARTICLE_INPUT = articleService.createArticle();

        StepVerifier.create(MOCK_ARTICLE_INPUT)
                .expectNextMatches(articleInput -> articleInput.getArticleCount() == 10)
                .verifyComplete();

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}