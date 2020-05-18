package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.Article;
import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import com.molam0la.dev.newsapi.config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Component
@Service
public class ArticleService {

    private GNews gNews;

    private ConfigProps configProps;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    public ArticleService(GNews gNews, ConfigProps configProps) {
        this.gNews = gNews;
        this.configProps = configProps;
    }

    public Mono<ArticleInput> getArticlesByTopic() {
        return gNews.createWebClient()
                .get()
                .uri(createTopicUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<ArticleInput> getArticlesBySearchWord() {
        return gNews.createWebClient()
                .get()
                .uri(createSearchUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<Tuple2<ArticleInput, ArticleInput>> combineArticleInput() {
        return Mono.zip(getArticlesByTopic(), getArticlesBySearchWord());
    }

    public Mono<List<Article>> createListOfArticles(Mono<ArticleInput> articleInput) {
        return articleInput.map(ArticleInput::getArticles);
    }


    private String createTopicUrl() {
        String url = "topics/" + configProps.getTopic() + "?token=" + configProps.getApikey();
        return configProps.getLang().isEmpty() ? url : url + "&lang=" + configProps.getLang();
    }

    private String createSearchUrl() {
        String url = "search?q=" + configProps.getKeyword() + "&token=" + configProps.getApikey();
        return configProps.getLang().isEmpty() ? url : url + "&lang=" + configProps.getLang();
    }


}
