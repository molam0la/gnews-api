package com.molam0la.dev.gnews_api;

import com.molam0la.dev.gnews_api.cassandra.repository.DBArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DBArticleService {

    private static final Logger log = LoggerFactory.getLogger(DBArticleService.class);

    private final GNewsArticleService gNewsArticleService;
    private final DBArticleRepository dbArticleRepository;

    public DBArticleService(GNewsArticleService gNewsArticleService, DBArticleRepository dbArticleRepository) {
        this.gNewsArticleService = gNewsArticleService;
        this.dbArticleRepository = dbArticleRepository;
    }

    @PostConstruct
    public void saveTopicArticlesInDB() {
        gNewsArticleService.getTopicArticlesForCaching()
                .subscribe(dbArticles -> dbArticles.forEach(dbArticleRepository::save),
                        error -> log.error("Error during saving articles for caching " + error.getMessage())
                );
    }
}
