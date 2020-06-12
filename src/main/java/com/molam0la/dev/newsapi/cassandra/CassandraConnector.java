package com.molam0la.dev.newsapi.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CassandraConnector {

    private Cluster cluster;

    private Session session;

    private Logger log = LoggerFactory.getLogger(CassandraConnector.class);

    public void connect(String host, int port) throws IOException {
        this.cluster = Cluster.builder()
                .addContactPoint(host)
                .withPort(port)
                .withoutMetrics()
                .build();

        Metadata metadata = cluster.getMetadata();

        log.info("Connected to cluster: " + metadata.getClusterName());

        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void cleanData() {
    }

    public void close() {
        cluster.close();
    }
}
