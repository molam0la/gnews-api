package com.molam0la.dev.newsapi.Articles;

import java.time.Instant;

public class ClientArticle {

    private String title;
    private String description;
    private String url;
    private String image;
    private Instant publishedAt;
    private String sourceName;
    private String sourceUrl;

    public ClientArticle(String title, String description, String url, String image, Instant publishedAt, String sourceName, String sourceUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
        this.publishedAt = publishedAt;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
