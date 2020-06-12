package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.article_props.ArticleInput;
import com.molam0la.dev.newsapi.articles.ClientArticleInput;
import com.molam0la.dev.newsapi.articles.ClientArticle;
import com.molam0la.dev.newsapi.mappers.GNewsArticleToClientArticleMapper;
import com.molam0la.dev.newsapi.app_config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GNewsArticleService {

    private GNews gNews;
    private ConfigProps configProps;
    private GNewsArticleToClientArticleMapper gnewsArticleToClientArticleMapper;

    private static final Logger log = LoggerFactory.getLogger(GNewsArticleService.class);

    public GNewsArticleService(GNews gNews, ConfigProps configProps, GNewsArticleToClientArticleMapper gnewsArticleToClientArticleMapper) {
        this.gNews = gNews;
        this.configProps = configProps;
        this.gnewsArticleToClientArticleMapper = gnewsArticleToClientArticleMapper;
    }

    public Mono<ClientArticleInput> getArticlesByTopic() {
        return gNews.createWebClient()
                .get()
                .uri(createTopicUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .map(gnewsArticleToClientArticleMapper)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<ClientArticleInput> getArticlesBySearchWord() {
        return gNews.createWebClient()
                .get()
                .uri(createSearchUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .map(gnewsArticleToClientArticleMapper)
                .doOnError(error -> log.error(error.getMessage())).onErrorResume(error -> {
                    if (error.getMessage().contains("429")) {
                        log.error("Gnews request limit has been reached today.");
                    }
                    return Mono.error(error);
                });
    }

    public Mono<List<ClientArticle>> createListOfArticles(Mono<ClientArticleInput> articleInput) {
        return articleInput.map(ClientArticleInput::getClientArticles);
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
