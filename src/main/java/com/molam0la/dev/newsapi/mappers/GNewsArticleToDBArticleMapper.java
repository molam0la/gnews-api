package com.molam0la.dev.newsapi.mappers;

import com.molam0la.dev.newsapi.article_props.ArticleInput;
import com.molam0la.dev.newsapi.app_config.ConfigProps;
import com.molam0la.dev.newsapi.cassandra.model.DBArticle;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class GNewsArticleToDBArticleMapper implements BiFunction<ArticleInput, ConfigProps, List<DBArticle>> {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public List<DBArticle> apply(ArticleInput articleInput, ConfigProps configProps) {

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
