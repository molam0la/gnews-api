package com.molam0la.dev.newsapi.mappers;

import com.molam0la.dev.newsapi.article_props.ArticleInput;
import com.molam0la.dev.newsapi.articles.ClientArticle;
import com.molam0la.dev.newsapi.articles.ClientArticleInput;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GNewsArticleToClientArticleMapper implements Function<ArticleInput, ClientArticleInput> {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public ClientArticleInput apply(ArticleInput articleInput) {

        int timestamp = articleInput.getTimestamp();
        int articleCount = articleInput.getArticleCount();

        List<ClientArticle> articles =
                articleInput.getArticles()
                        .stream()
                        .map(article ->
                                new ClientArticle(
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

        return new ClientArticleInput(timestamp, articleCount, articles);
    }

    private Instant convertTimestamp(String timestamp) {
        return Instant.from(dateTimeFormatter.parse(timestamp));
    }
}
