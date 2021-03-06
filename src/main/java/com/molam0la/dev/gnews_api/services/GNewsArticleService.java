package com.molam0la.dev.gnews_api.services;

import com.molam0la.dev.gnews_api.GNews;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import com.molam0la.dev.gnews_api.article_props.ArticleInput;
import com.molam0la.dev.gnews_api.articles.ClientArticle;
import com.molam0la.dev.gnews_api.articles.ClientArticleInput;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import com.molam0la.dev.gnews_api.cassandra.repository.DBArticleRepository;
import com.molam0la.dev.gnews_api.mappers.DBArticleToClientArticleMapper;
import com.molam0la.dev.gnews_api.mappers.GNewsArticleToClientArticleMapper;
import com.molam0la.dev.gnews_api.mappers.GNewsArticleToDBArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GNewsArticleService {

    private final GNews gNews;
    private final ConfigProps configProps;
    private final GNewsArticleToClientArticleMapper gnewsArticleToClientArticleMapper;
    private final GNewsArticleToDBArticleMapper gNewsArticleToDBArticleMapper;
    private final DBArticleToClientArticleMapper dbArticleToClientArticleMapper;
    private final DBArticleRepository repository;

    private static final Logger log = LoggerFactory.getLogger(GNewsArticleService.class);

    public GNewsArticleService(GNews gNews,
                               ConfigProps configProps,
                               GNewsArticleToClientArticleMapper gnewsArticleToClientArticleMapper,
                               GNewsArticleToDBArticleMapper gNewsArticleToDBArticleMapper,
                               DBArticleToClientArticleMapper dbArticleToClientArticleMapper,
                               DBArticleRepository repository) {
        this.gNews = gNews;
        this.configProps = configProps;
        this.gnewsArticleToClientArticleMapper = gnewsArticleToClientArticleMapper;
        this.gNewsArticleToDBArticleMapper = gNewsArticleToDBArticleMapper;
        this.dbArticleToClientArticleMapper = dbArticleToClientArticleMapper;
        this.repository = repository;
    }

    public Mono<ClientArticleInput> getArticlesByTopic() {
        return gNews.createWebClient()
                .get()
                .uri(createTopicUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .doOnNext(articleInput -> {
                    log.info("Caching articles during GNews api call");
                    repository.saveAll(gNewsArticleToDBArticleMapper.apply(articleInput));
                })
                .map(gnewsArticleToClientArticleMapper)
                .onErrorResume(throwable -> {
                    log.error("Unable to retrieve articles from GNews api", throwable);
                    return Mono.fromCallable(() -> repository.findAllArticlesByTopic(configProps.getTopic()))
                            .flatMap(dbArticles -> {
                                if (dbArticles.iterator().hasNext()) {
                                    return Mono.just(dbArticleToClientArticleMapper.apply(dbArticles));
                                } else {
                                    return Mono.<ClientArticleInput>error(new Exception("Unable to retrieve articles from cache"));
                                }
                            });
                });
    }

    public Mono<List<DBArticle>> getTopicArticlesForUpfrontCaching() {
        return gNews.createWebClient()
                .get()
                .uri(createTopicUrl())
                .retrieve()
                .bodyToMono(ArticleInput.class)
                .map(gNewsArticleToDBArticleMapper)
                .doOnError(error -> log.error("Error retrieving articles for caching " + error.getMessage()));
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
