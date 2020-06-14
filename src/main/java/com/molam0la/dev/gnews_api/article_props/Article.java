package com.molam0la.dev.gnews_api.article_props;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Article {

    private @JsonProperty String title;
    private @JsonProperty String description;
    private @JsonProperty String url;
    private @JsonIgnore String image;
    private @JsonProperty String publishedAt;
    private @JsonProperty Source source;

    public Article(String title, String description, String url, String image, String publishedAt, Source source) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
        this.publishedAt = publishedAt;
        this.source = source;
    }

    public Article() {
        super();
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

    public Source getSource() {
        return source;
    }
}