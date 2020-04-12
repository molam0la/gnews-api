package com.molam0la.dev.newsapi.ArticleProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Article {

    private @JsonProperty String title;
    private @JsonProperty String description;
    private @JsonProperty String url;
    private @JsonIgnore String image;
    private @JsonProperty String publishedAt;
    private @JsonIgnore List<String> source;
//    private @JsonProperty String sourceName;
//    private @JsonProperty String sourceUrl;

    public Article() {
        super();
    }

    public Article(String title, String description, String url, String image, String publishedAt, List<String> source) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
        this.publishedAt = publishedAt;
        this.source = source;
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

    public String getPublishedAt() {
        return publishedAt;
    }

    public List<String> getSource() {
        return source;
    }
}