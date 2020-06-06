package com.molam0la.dev.newsapi.Cassandra;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmbeddedCassandraTest.class)
public class EmbeddedCassandraTest {

    private static Logger log = LoggerFactory.getLogger(CassandraConnector.class);

    private static Session session;

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS " +
            "gnews_api WITH replication = {'class':'SimpleStrategy', 'replication_factor':'1'};";

    public static final String KEYSPACE_ACTIVATE_QUERY = "USE gnews_api;";

    public static final String TABLE_CREATION_QUERY = "CREATE TABLE articles (\n" +
            "id int,\n" +
            "topic text,\n" +
            "title text,\n" +
            "description text,\n" +
            "url text,\n" +
            "published_at timestamp,\n" +
            "source text,\n" +
            "source_url text,\n" +
            "PRIMARY KEY (topic, published_at))\n" +
            "WITH CLUSTERING ORDER BY (published_at DESC);";

    public static final String TABLE_INSERT_QUERY = "INSERT into articles(published_at, topic, description, source, source_url, title, url) values ('2020-04-01T11:21:59.001+0000', 'dog', 'Happy birthday dog memes', 'Woman’s Day', 'https://www.womansday.com', 'Memes For Dogs', 'https://www.womansday.com');";

    public static final String DATA_TABLE_NAME = "articles";

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
        session.execute(TABLE_CREATION_QUERY);
        log.info("TABLE CREATED. INSERTING DATA INTO THE TABLE");
        session.execute(TABLE_INSERT_QUERY);
    }

//    @Ignore
    @Test
    public void testResults() {
        List<Row> results = session.execute("SELECT * FROM gnews_api.articles;").all();
        results.stream().forEach(row -> System.out.println(row));
    }

    @AfterClass
    public static void stopEmbeddedCassandra() {
        log.info("CLEANING AND CLOSING EMBEDDED CASSANDRA");
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
