package com.molam0la.dev.newsapi.mappers;

import com.molam0la.dev.newsapi.articles.ClientArticle;
import com.molam0la.dev.newsapi.cassandra.model.DBArticle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DBArticleToClientArticleMapper implements Function<List<DBArticle>, List<ClientArticle>> {

    @Override
    public List<ClientArticle> apply(List<DBArticle> dbArticles) {

        return dbArticles.
                stream()
                .map(dbArticle ->
                        new ClientArticle(
                                dbArticle.getTitle(),
                                dbArticle.getDescription(),
                                dbArticle.getUrl(),
                                null,
                                dbArticle.getPublished_at(),
                                dbArticle.getSource(),
                                dbArticle.getSource_url()
                        )
                )
                .collect(Collectors.toList());
    }
}
