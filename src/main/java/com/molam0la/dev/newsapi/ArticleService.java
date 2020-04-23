package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.Article;
import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Service
//service on app start up an instance of this class will be created
public class ArticleService {

    private GNews gNews;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    public ArticleService(GNews gNews) {
        this.gNews = gNews;
    }

    public Mono<ArticleInput> retrieveAllArticles() {
        return gNews.createWebClient()
                .get()
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<List<Article>> createListOfArticles() {
        return retrieveAllArticles().map(ArticleInput::getArticles);
    }

    public Mono<Stream<Article>> showNumberOfArticles(int count) {
        return createListOfArticles().map(articles -> articles.stream().limit(count));
    }

    public Mono<List<String>> createListOfTitles() {
        return createListOfArticles()
                .map(articles -> articles.stream().map(Article::getTitle).collect(Collectors.toList()));
    }

}
