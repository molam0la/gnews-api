package com.molam0la.dev.gnews_api.services;

import com.molam0la.dev.gnews_api.cassandra.repository.DBArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Service
public class DBArticleService {

    private static final Logger log = LoggerFactory.getLogger(DBArticleService.class);

    private static final Duration TTL = Duration.ofHours(24);

    private GNewsArticleService gNewsArticleService;
    private DBArticleRepository dbArticleRepository;

    public DBArticleService(GNewsArticleService gNewsArticleService, DBArticleRepository dbArticleRepository) {
        this.gNewsArticleService = gNewsArticleService;
        this.dbArticleRepository = dbArticleRepository;
    }

    @PostConstruct
    public void prepareDbOnStartup() {
            gNewsArticleService.getTopicArticlesForUpfrontCaching()
                    .subscribe(
                            dbArticles -> {
                                log.info("Caching articles on startup...");
                                dbArticles.forEach(dbArticleRepository::save);
                            },
                            error -> log.error("Error during saving articles for caching", error)
                    );

            log.info("Removing stale articles...");
            dbArticleRepository.findAll().stream()
                    .filter(dbArticle -> isStale(dbArticle.getPublished_at()))
                    .forEach(dbArticleRepository::delete);
        }

    private boolean isStale(Instant timestamp) {
        Duration hoursSinceTimestamp = Duration.ofHours(Duration.between(timestamp, Instant.now()).toHours());
        return hoursSinceTimestamp.compareTo(TTL) > 0;
    }
}
