package com.molam0la.dev.newsapi.Models;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ArticleToModelMapper implements Function<ArticleInput, ArticleInputModel> {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public ArticleInputModel apply(ArticleInput articleInput) {

        int timestamp = articleInput.getTimestamp();
        int articleCount = articleInput.getArticleCount();

        List<ArticleModel> articles =
                articleInput.getArticles()
                        .stream()
                        .map(article ->
                                new ArticleModel(
                                        article.getTitle(),
                                        article.getDescription(),
                                        article.getUrl(),
                                        article.getImage(),
                                        convertTimestamp(article.getPublishedAt()),
                                        article.getSource().getName(),
                                        article.getSource().getUrl()
                                )
                        )
                        .collect(Collectors.toList());

        return new ArticleInputModel(timestamp, articleCount, articles);
    }

    private Instant convertTimestamp(String timestamp) {
        return Instant.from(dateTimeFormatter.parse(timestamp));
    }
}
