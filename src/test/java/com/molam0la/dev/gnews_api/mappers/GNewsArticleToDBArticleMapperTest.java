package com.molam0la.dev.gnews_api.mappers;

import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import com.molam0la.dev.gnews_api.article_props.Article;
import com.molam0la.dev.gnews_api.article_props.ArticleInput;
import com.molam0la.dev.gnews_api.article_props.Source;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = ConfigProps.class)
@ActiveProfiles("test")
class GNewsArticleToDBArticleMapperTest {

    private GNewsArticleToDBArticleMapper mapper;
    private ArticleInput articleInput;
    private List<Article> articlesList;

    @Autowired
    private ConfigProps configProps;

    @BeforeEach
    public void setUp() {
        mapper = new GNewsArticleToDBArticleMapper(configProps);

        articlesList = new ArrayList<>();
        articlesList.add(new Article("title1", "description1", "url1", "image1", "2020-03-27 19:30:21 UTC", new Source("source1", "sourceUrl1")));
        articlesList.add(new Article("title2", "description2", "url2", "image2", "2020-03-27 19:30:21 UTC", new Source("source2", "sourceUrl2")));

        articleInput = new ArticleInput(12345, 2, articlesList);
    }

    @Test
    public void apply_ReturnsCorrectClass() {
        assertThat(mapper.apply(articleInput).equals(DBArticle.class));
    }

    @Test
    public void apply_ReturnsAListOfDBArticles() {
        assertFalse(mapper.apply(articleInput).isEmpty());
    }

    @Test
    public void apply_ReturnsTopicValueFromConfigMap() {
        assertEquals("testTopic", mapper.apply(articleInput).get(0).getTopic());
    }

    @Test
    public void apply_ReturnsDBArticlesWithIdsFromTheRange() {
        assertThat((mapper.apply(articleInput).get(0).getId() >= 0));
        assertThat((mapper.apply(articleInput).get(0).getId() <= 1000));
    }

    @Test
    public void apply_ReturnsDbArticlesWithCorrectPublishedAtValueConvertedToISOFormat() {
        assertEquals(ZonedDateTime.of(2020,3,27,19,30,21,0, ZoneOffset.UTC).toInstant(),
                mapper.apply(articleInput).get(0).getPublished_at());
    }
}