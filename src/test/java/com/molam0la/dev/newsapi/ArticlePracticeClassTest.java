package com.molam0la.dev.newsapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ArticlePracticeClassTest {

    private BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/sandra/Documents/Dev/news-api/src/test/java/articleMock.json"));
    private Mono<String> mockArticle = Mono.just(bufferedReader.readLine());
    private ArticlePracticeClass articlePracticeClass;

    @Mock
    NewsApi1 newsApi1;

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    WebClient.Builder webClientBuilder;

    ArticlePracticeClassTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        newsApi1 = new NewsApi1(webClientBuilder);
        articlePracticeClass = new ArticlePracticeClass(newsApi1);

        given(webClientBuilder.baseUrl(any())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(String.class)).willReturn(mockArticle);

        articlePracticeClass.setAllValues();

    }

    @Test
    void testNewsApiCallResponseIsNotEmpty() {
        assertThat(!newsApi1.makeRequest().block().isEmpty());
    }

    @Test
    void testNewsApiCallResponseHasTimeStamp() {
        assertThat(articlePracticeClass.getTimeStamp()).isEqualTo(1585339920);
    }

    @Test
    void testNewsApiCallResponseHasArticleCount() {
        assertThat(articlePracticeClass.getArticleCount()).isEqualTo(10);
    }

    @Test
    void testNewsApiCallResponseArticleTitleIsCorrect() {
        assertThat(articlePracticeClass.getArticleTitles().get(0).equals("Italy\u2019s Slow Progress in Fighting Coronavirus Is a Warning to West"));
    }

    @Test
    void testNewsApiCallResponsePublishesTimestampIsCorrect() {
        assertThat(articlePracticeClass.getPublishedTimestamp().get(1).equals("2020-03-27 19:30:21 UTC"));
    }

    @Test
    void testNewsApiCallResponseSourceIsCorrect() {
        assertThat(articlePracticeClass.getPublishedTimestamp().get(0).equals("The Wall Street Journal"));
    }
}