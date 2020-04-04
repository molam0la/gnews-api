package com.molam0la.dev.newsapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NewsApi1Test {

    private NewsApi1 newsApi1;

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    WebClient.Builder webClientBuilder;

    @BeforeEach
    void setUp() {
        newsApi1 = new NewsApi1(webClientBuilder);
        given(webClientBuilder.baseUrl(any())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);

    }

    @Test
    void testMakingRequestReceivesBody() {
        Mono<String> body = Mono.just("body");
        given(responseSpec.bodyToMono(String.class)).willReturn(body);
        Mono<String> response = newsApi1.makeRequest();

        StepVerifier.create(response).expectNext("body").expectComplete().verify();

    }

    @Test
    void testReadingHeadersFromResponseBody() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/sandra/Documents/Dev/news-api/src/test/java/articleMock.json"));
        Mono<String> mockArticle = Mono.just(bufferedReader.readLine());

        given(responseSpec.bodyToMono(String.class)).willReturn(mockArticle);
        Mono<String> response = newsApi1.makeRequest();

//        StepVerifier.create(response).expectNext("timestamp").expectComplete().verify();

    }

}