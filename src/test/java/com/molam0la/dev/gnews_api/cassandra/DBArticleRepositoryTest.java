package com.molam0la.dev.gnews_api.cassandra;

import com.datastax.driver.core.Session;
import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import com.molam0la.dev.gnews_api.cassandra.repository.DBArticleRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
@SpringBootTest(classes = CassandraConfig.class)
@ActiveProfiles("test")
public class DBArticleRepositoryTest {

    private static Logger log = LoggerFactory.getLogger(CassandraConnector.class);

    private static Session session;

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS gnews_api WITH replication = {'class':'SimpleStrategy', 'replication_factor':'1'};";
    public static final String KEYSPACE_ACTIVATE_QUERY = "USE gnews_api;";
    public static final String TABLE_NAME = "dbarticle";

    @Autowired
    private DBArticleRepository repository;

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @Autowired
    private CassandraOperations cassandraTemplate;

    @BeforeClass
    public static void startEmbeddedCassandra() throws IOException, InterruptedException, ConfigurationException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("cassandra.yaml");
        log.info("Initialising embedded cassandra");
        session = EmbeddedCassandraServerHelper.getSession();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        log.info("Keyspace created and activated");
        Thread.sleep(5000);
    }

    @Before
    public void createTable() {
        adminTemplate.createTable(true, CqlIdentifier.of(TABLE_NAME), DBArticle.class, new HashMap<String, Object>());
    }

    @Test
    public void insert_AndRetrieveADBArticleFromTable() {
        final DBArticle article1 = new DBArticle(
                1,
                "topic",
                "Test Title",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");

        repository.save(article1);
        final Iterable<DBArticle> articles = repository.findAllArticles();
        assertEquals(article1.getTitle(), articles.iterator().next().getTitle());
    }

    @Test
    public void delete_DBArticleRemovesRowFromTable() {
        final DBArticle article1 = new DBArticle(
                1,
                "topic",
                "Test Title1",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");

        final DBArticle article2 = new DBArticle(
                2,
                "topic2",
                "Test Title2",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 3, 25, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");

        repository.insert(article1);
        repository.insert(article2);
        repository.delete(article1);
        assertEquals(1, cassandraTemplate.count(DBArticle.class));
    }

    @Test
    public void delete_AllDBArticlesRemovesAllRowsFromTable() {
        repository.insert(generateListOfTestArticles().get(0));
        repository.insert(generateListOfTestArticles().get(1));
        cassandraTemplate.truncate(DBArticle.class);
        assertEquals(0, cassandraTemplate.count(DBArticle.class));
    }

    @Test
    public void findAllArticlesByTopic_ReturnsCorrectArticles() {
        repository.insert(generateListOfTestArticles().get(0));
        repository.insert(generateListOfTestArticles().get(1));
        String expectedTopic = generateListOfTestArticles().get(0).getTopic();
        assertEquals(expectedTopic, repository.findAllArticlesByTopic(expectedTopic));
    }

    private List<DBArticle> generateListOfTestArticles() {
        DBArticle article1 = new DBArticle(
                1,
                "world",
                "Test Title1",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 3, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");

        DBArticle article2 = new DBArticle(
                2,
                "technology",
                "Test Title2",
                "Description for Test Title",
                "www.test.com",
                ZonedDateTime.of(2020, 2, 27, 19, 30, 21, 0, ZoneOffset.UTC).toInstant(),
                "Test Source",
                "www.test-source.com");

        return Arrays.asList(article1, article2);
    }

    @AfterEach
    public void deleteTable() {
        adminTemplate.dropTable(CqlIdentifier.of(TABLE_NAME));
    }

    @AfterClass
    public static void stopEmbeddedCassandra() {
        log.info("CLEANING AND CLOSING EMBEDDED CASSANDRA");
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
