package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import com.molam0la.dev.newsapi.Models.ArticleInputModel;
import com.molam0la.dev.newsapi.Models.ArticleModel;
import com.molam0la.dev.newsapi.Models.ArticleToModelMapper;
import com.molam0la.dev.newsapi.config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ArticleService {

    private GNews gNews;

    private ConfigProps configProps;
    private ArticleToModelMapper articleToModelMapper;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    public ArticleService(GNews gNews, ConfigProps configProps, ArticleToModelMapper articleToModelMapper) {
        this.gNews = gNews;
        this.configProps = configProps;
        this.articleToModelMapper = articleToModelMapper;
    }

    public Mono<ArticleInputModel> getArticlesByTopic() {
        return gNews.createWebClient()
                .get()
                .uri(createTopicUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .map(articleToModelMapper)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<ArticleInputModel> getArticlesBySearchWord() {
        return gNews.createWebClient()
                .get()
                .uri(createSearchUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .map(articleToModelMapper)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<List<ArticleModel>> createListOfArticles(Mono<ArticleInputModel> articleInput) {
        return articleInput.map(ArticleInputModel::getArticleModels);
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
