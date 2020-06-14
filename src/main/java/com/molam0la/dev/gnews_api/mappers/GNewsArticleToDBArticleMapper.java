package com.molam0la.dev.gnews_api.mappers;

import com.molam0la.dev.gnews_api.article_props.ArticleInput;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GNewsArticleToDBArticleMapper implements Function<ArticleInput, List<DBArticle>> {

    private ConfigProps configProps;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public GNewsArticleToDBArticleMapper(ConfigProps configProps) {
        this.configProps = configProps;
    }

    @Override
    public List<DBArticle> apply(ArticleInput articleInput) {

        return articleInput.getArticles()
                .stream()
                .map(article ->
                        new DBArticle(
                                (int)(Math.random() * 1000),
                                configProps.getTopic(),
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrl(),
                                convertTimestamp(article.getPublishedAt()),
                                article.getSource().getName(),
                                article.getSource().getUrl()
                        )

                )
                .collect(Collectors.toList());
    }

    private Instant convertTimestamp(String timestamp) {
        return Instant.from(dateTimeFormatter.parse(timestamp));
    }
}
