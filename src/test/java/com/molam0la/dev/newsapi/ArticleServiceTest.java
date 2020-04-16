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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private GNews gNews;
    private ArticleService articleService;
    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    private String MOCK_ARTICLE;

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
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/articleMock.json"));
        MOCK_ARTICLE = bufferedReader.readLine();

        //set mockresponse
        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody(MOCK_ARTICLE);
        mockWebServer.enqueue(mockResponse);
    }

    @Test
    void testGettingResponseFromWebClientWithCorrectArticleCount() {

        StepVerifier.create(articleService.retrieveAllArticles())
                .expectNextMatches(articleInput -> articleInput.getArticleCount() == 10)
                .verifyComplete();
    }

    @Test
    void testReturningFirstTitleFromTheListOfTitles() {
        Mono<List<String>> mockTitlesList = articleService.createListOfTitles();

        assertThat(mockTitlesList
                .block().get(0).equals("Italy’s Slow Progress in Fighting Coronavirus Is a Warning to West"));

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}