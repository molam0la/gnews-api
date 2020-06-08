package com.molam0la.dev.newsapi.Cassandra;

import com.datastax.driver.core.Session;
import com.molam0la.dev.newsapi.Articles.DBArticle;
import com.molam0la.dev.newsapi.Articles.DBArticleRepository;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmbeddedCassandraTest.class)
public class EmbeddedCassandraTest {

    private static Logger log = LoggerFactory.getLogger(CassandraConnector.class);

    private static Session session;
    private com.molam0la.dev.newsapi.Articles.DBArticleRepository DBArticleRepository;

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS gnews_api WITH replication = {'class':'SimpleStrategy', 'replication_factor':'1'};";
    public static final String KEYSPACE_ACTIVATE_QUERY = "USE gnews_api;";
    public static final String TABLE_NAME = "articles";

    @BeforeClass
    public static void startEmbeddedCassandra() throws IOException, InterruptedException, ConfigurationException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("cassandra.yaml");
        session = EmbeddedCassandraServerHelper.getSession();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        Thread.sleep(5000);
    }

    @Before
    public void createTable() {
        DBArticleRepository = new DBArticleRepository(session);
        DBArticleRepository.createTable();
    }

    @Test
    public void insertCachedArticleIntoDBIsSuccessfulForAllFields() {
        DBArticle DBArticle = new DBArticle(
                1,
                "topic",
                "Test Title",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");
        DBArticleRepository.insertArticle(DBArticle);

        assertEquals(1, DBArticleRepository.selectAllCachedArticles().get(0).getId());
        assertEquals("topic", DBArticleRepository.selectAllCachedArticles().get(0).getTopic());
        assertEquals("Test Title", DBArticleRepository.selectAllCachedArticles().get(0).getTitle());
        assertEquals("Description for Test Title", DBArticleRepository.selectAllCachedArticles().get(0).getDescription());
        assertEquals("www.test.com", DBArticleRepository.selectAllCachedArticles().get(0).getUrl());
        assertEquals(ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(), DBArticleRepository.selectAllCachedArticles().get(0).getPublished_at());
        assertEquals("Test Source", DBArticleRepository.selectAllCachedArticles().get(0).getSource());
        assertEquals("www.test-source.com", DBArticleRepository.selectAllCachedArticles().get(0).getSource_url());
    }

    @AfterEach
    public void deleteTable() {
        DBArticleRepository.deleteTable(TABLE_NAME);
    }

    @AfterClass
    public static void stopEmbeddedCassandra() {
        log.info("CLEANING AND CLOSING EMBEDDED CASSANDRA");
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
