package com.molam0la.dev.gnews_api.services;

import com.molam0la.dev.gnews_api.GNews;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import com.molam0la.dev.gnews_api.articles.ClientArticle;
import com.molam0la.dev.gnews_api.articles.ClientArticleInput;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import com.molam0la.dev.gnews_api.cassandra.repository.DBArticleRepository;
import com.molam0la.dev.gnews_api.mappers.DBArticleToClientArticleMapper;
import com.molam0la.dev.gnews_api.mappers.GNewsArticleToClientArticleMapper;
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
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;

@ExtendWith(MockitoExtension.class)
class GNewsArticleServiceTest {

    private GNewsArticleService gNewsArticleService;
    private GNewsArticleToClientArticleMapper gNewsArticleToClientArticleMapper;
    private GNewsArticleToDBArticleMapper gNewsArticleToDBArticleMapper;
    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    private String ARTICLE_STUB;

    @Mock
    private GNews gNews;
    @Mock
    private ConfigProps configProps;
    @Mock
    private DBArticleToClientArticleMapper dbArticleToClientArticleMapper;
    @Mock
    private DBArticleRepository repository;

    private ClientArticleInput clientArticleInput;

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
        gNewsArticleToClientArticleMapper = new GNewsArticleToClientArticleMapper();
        gNewsArticleToDBArticleMapper = new GNewsArticleToDBArticleMapper(configProps);
        gNewsArticleService = new GNewsArticleService(gNews, configProps, gNewsArticleToClientArticleMapper,
                gNewsArticleToDBArticleMapper, dbArticleToClientArticleMapper, repository);
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

        StepVerifier.create(gNewsArticleService.getArticlesByTopic())
                .expectNextMatches(articleInput -> articleInput.getArticleCount() == 10)
                .verifyComplete();
    }

    @Test
    void getArticlesBySearchWord_returnsCorrectTimestamp() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(gNewsArticleService.getArticlesBySearchWord())
                .expectNextMatches(articleInput -> articleInput.getTimestamp() == 1585339920)
                .verifyComplete();
    }

    @Test
    void getArticlesBySearchWord_returnsCorrectSourceName() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        assertTrue(gNewsArticleService
                .createListOfArticles(gNewsArticleService.getArticlesBySearchWord())
                .block()
                .get(0)
                .getSourceName()
                .equals("The Wall Street Journal"));
    }

    @Test
    void getTopicArticlesForCaching_ReturnsAMonoListOfDBArticles() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(gNewsArticleService.getTopicArticlesForUpfrontCaching())
                .expectNextMatches(dbArticles -> dbArticles.stream().findFirst().get().getTitle().equals("Italy’s Slow Progress in Fighting Coronavirus Is a Warning to West"))
                .verifyComplete();
    }

    @Test
    void getArticlesByTopic_ReturnsCachedArticlesForChosenTopicOnErrorWhenFindAllArticlesFromDBIsSuccessful() {
        mockResponse.setResponseCode(500);
        mockWebServer.enqueue(mockResponse);

        List<DBArticle> dbArticles = Arrays.asList(
                new DBArticle(1, "dog", "Some dog", "Some description", "www.dog.com", ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(), "Some source", "www.source-url"),
                new DBArticle(2, "cat", "Some cat", "Some description", "www.cat.com", ZonedDateTime.of(2020, 3, 22, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(), "Some source", "www.source-url"));
        List<ClientArticle> clientArticles = Collections.singletonList(new ClientArticle("Some dog", "Some description", "www.dog.com", null, Instant.now(), "Some source", "www.source-url"));

        clientArticleInput = new ClientArticleInput(100, 1, clientArticles);

        given(configProps.getTopic()).willReturn("testTopic");
        given(repository.findAllArticlesByTopic("testTopic")).willReturn(dbArticles);
        given(dbArticleToClientArticleMapper.apply(dbArticles)).willReturn(clientArticleInput);

        StepVerifier.create(gNewsArticleService.getArticlesByTopic())
                .expectNext(clientArticleInput)
                .verifyComplete();
    }

    @Test
    void getArticlesByTopic_ReturnsErrorWhenFindAllArticlesFromDBIsNotSuccessful() {
        mockResponse.setResponseCode(500);
        mockWebServer.enqueue(mockResponse);

        given(configProps.getTopic()).willReturn("testTopic");
        given(repository.findAllArticlesByTopic("testTopic")).willReturn(new ArrayList<>());

        StepVerifier.create(gNewsArticleService.getArticlesByTopic())
                .expectError(Exception.class)
                .verify();
    }

    @Test
    void getTopicArticlesForCaching_UsesTopicFromConfigProps() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        given(configProps.getTopic()).willReturn("testTopic");

        StepVerifier.create(gNewsArticleService.getTopicArticlesForUpfrontCaching())
                .expectNextMatches(dbArticles -> dbArticles.stream().findFirst().get().getTopic().equals("testTopic"))
                .verifyComplete();
    }

    @Test
    void getArticlesBySearchWord_returns4xxError() {
        mockResponse.setResponseCode(404);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(gNewsArticleService.createListOfArticles(gNewsArticleService.getArticlesBySearchWord()))
                .expectError(NotFound.class).verify();
    }

    @Test
    void getArticlesBySearchWord_returns5xxError() {
        mockResponse.setResponseCode(500);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(gNewsArticleService.createListOfArticles(gNewsArticleService.getArticlesBySearchWord()))
                .expectError(InternalServerError.class).verify();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}