package com.molam0la.dev.gnews_api.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("cassandra")
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {

    private Logger log = LoggerFactory.getLogger(CassandraConfig.class);

    private String keyspaceName;
    private String contactPoint;
    private int port;

    public String getKeyspaceName() { return keyspaceName; }

    public String getContactPoint() {
        return contactPoint;
    }

    public int getPort() {
        return port;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    @Bean
    public CassandraClusterFactoryBean cluster() {
        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactPoint);
        cluster.setPort(port);
        cluster.setMetricsEnabled(false);
        log.info("Cluster created with contact points: " + contactPoint + " & port: " + port);
        return cluster;
    }

    @Override
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new CassandraMappingContext();
    }

}
