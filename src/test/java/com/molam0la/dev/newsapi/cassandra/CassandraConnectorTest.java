package com.molam0la.dev.newsapi.cassandra;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.util.List;
//
//class CassandraConnectorTest {
//
//    private CassandraConnector client;
//    private Session session;
//
//    @Ignore
//    @Test
//    void connect() throws IOException {
//        client = new CassandraConnector();
//        client.connect("127.0.0.1", 9042);
//        session = client.getSession();
//        List<Row> results = session.execute("SELECT * FROM gnews_api.articles;").all();
//        results.stream().forEach(row -> System.out.println(row));
//    }
//}