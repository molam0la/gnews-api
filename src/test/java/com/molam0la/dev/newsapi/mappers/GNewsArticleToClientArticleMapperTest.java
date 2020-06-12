package com.molam0la.dev.newsapi.mappers;

import com.molam0la.dev.newsapi.article_props.Article;
import com.molam0la.dev.newsapi.article_props.ArticleInput;
import com.molam0la.dev.newsapi.article_props.Source;
import com.molam0la.dev.newsapi.articles.ClientArticleInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

class GNewsArticleToClientArticleMapperTest {

    private GNewsArticleToClientArticleMapper mapper;
    private ClientArticleInput clientArticleInput;
    private ArticleInput articleInput;
    private List<Article> articlesList;

    @BeforeEach
    public void setUp() throws IOException {

        mapper = new GNewsArticleToClientArticleMapper();

        articlesList = new ArrayList<>();
        articlesList.add(new Article("title1", "description1", "url1", "image1", "2020-03-27 19:30:21 UTC", new Source("source1", "sourceUrl1")));
        articlesList.add(new Article("title2", "description2", "url2", "image2", "2020-03-27 19:30:21 UTC", new Source("source2", "sourceUrl2")));

        articleInput = new ArticleInput(12345, 2, articlesList);
        clientArticleInput = mapper.apply(articleInput);
    }

    @Test
    public void applyReturnsAnInstanceOfArticleInputModel() {
        assertEquals(12345, clientArticleInput.getTimestamp());
        assertEquals(2, clientArticleInput.getArticleCount());
    }

    @Test
    public void applyReturnsArticleInputModelWithCorrectArticlePublishedAtValueConvertedToISOFormat() {
        assertEquals(ZonedDateTime.of(2020,3,27,19,30,21,0, ZoneOffset.UTC).toInstant(), clientArticleInput.getClientArticles().get(0).getPublishedAt());
    }

    @Test
    public void applyReturnsArticleInputModelWithCorrectArticleSourceNameAndSourceUrl() {
        assertEquals("source1", clientArticleInput.getClientArticles().get(0).getSourceName());
        assertEquals("sourceUrl1", clientArticleInput.getClientArticles().get(0).getSourceUrl());
    }
}