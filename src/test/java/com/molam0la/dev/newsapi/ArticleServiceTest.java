package com.molam0la.dev.newsapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private GNews GNews;
    private ArticleService articleService;

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    WebClient.Builder webClientBuilder;

    @BeforeEach
    void setUp() throws IOException {
        GNews = new GNews(webClientBuilder);
        articleService = new ArticleService(GNews);
        given(webClientBuilder.baseUrl(any())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/sandra/Documents/Dev/news-api/src/test/java/articleMock.json"));
//        Mono<Article> mockArticle = Mono.create(bufferedReader.readLine());
//        given(responseSpec.bodyToMono(String.class)).willReturn(mockArticle);
    }

    @Test
    public void generateArticleMono() {
        articleService.generateArticle();
    }

}