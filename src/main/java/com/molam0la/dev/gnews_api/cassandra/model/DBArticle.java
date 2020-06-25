package com.molam0la.dev.gnews_api.cassandra.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table
public class DBArticle {

    @Column
    private int id;
    @PrimaryKeyColumn(name ="topic", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String topic;
    @PrimaryKeyColumn(name ="title", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private String title;
    @Column
    private String description;
    @Column
    private String url;
    @PrimaryKeyColumn(name = "published_at", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant published_at;
    @Column
    private String source;
    @Column
    private String source_url;

    public DBArticle(int id, String topic, String title, String description, String url, Instant published_at, String source, String source_url) {
        this.id = id;
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.url = url;
        this.published_at = published_at;
        this.source = source;
        this.source_url = source_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Instant published_at) {
        this.published_at = published_at;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

}
