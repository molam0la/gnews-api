package com.molam0la.dev.newsapi.article_props;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArticleInput {

    private @JsonProperty int timestamp;
    private @JsonProperty int articleCount;
    private @JsonProperty List<Article> articles;

    public ArticleInput(int timestamp, int articleCount, List<Article> articles) {
        this.timestamp = timestamp;
        this.articleCount = articleCount;
        this.articles = articles;
    }

    public ArticleInput() {
        super();
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
