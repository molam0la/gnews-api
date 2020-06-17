package com.molam0la.dev.gnews_api.mappers;

import com.molam0la.dev.gnews_api.articles.ClientArticleInput;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DBArticleToClientArticleMapperTest {
    
    private DBArticleToClientArticleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DBArticleToClientArticleMapper();
    }

    @Test
    void applyReturnsAnInstanceOfClientArticleInput() {

        List<DBArticle> dbArticles = new ArrayList<>();
        dbArticles.add(new DBArticle(1, "dog", "Some dog", "Some description", "www.dog.com", ZonedDateTime.of(2020,3,27,19,30,21,0, ZoneOffset.UTC).toInstant(), "Some source", "www.source-url"));
        dbArticles.add(new DBArticle(2, "cat", "Some cat", "Some description", "www.cat.com", ZonedDateTime.of(2020,3,22,19,30,21,0, ZoneOffset.UTC).toInstant(), "Some source", "www.source-url"));

        Iterable<DBArticle> iterableDbArticles = dbArticles;
        ClientArticleInput testInput = mapper.apply(iterableDbArticles);

        assertEquals(testInput.getClientArticles().get(0).getTitle(), "Some dog");
    }
}