package com.molam0la.dev.gnews_api.mappers;

import com.molam0la.dev.gnews_api.articles.ClientArticle;
import com.molam0la.dev.gnews_api.articles.ClientArticleInput;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class DBArticleToClientArticleMapper implements Function<Iterable<DBArticle>, ClientArticleInput> {

    private static final Logger log = LoggerFactory.getLogger(DBArticleToClientArticleMapper.class);

    private List<ClientArticle> clientArticles = new ArrayList<>();

    @Override
    public ClientArticleInput apply(Iterable<DBArticle> dbArticles) {

        dbArticles
                .forEach(dbArticle -> {
                    clientArticles.add
                            (new ClientArticle(
                                            dbArticle.getTitle(),
                                            dbArticle.getDescription(),
                                            dbArticle.getUrl(),
                                            null,
                                            dbArticle.getPublished_at(),
                                            dbArticle.getSource(),
                                            dbArticle.getSource_url()
                                    )
                            );
                });
        log.info("Returning articles from cache");
        return new ClientArticleInput(Instant.now().getNano(), clientArticles.size(), clientArticles);
    }
}
