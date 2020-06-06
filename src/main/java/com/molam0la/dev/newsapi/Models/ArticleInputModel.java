package com.molam0la.dev.newsapi.Models;

import java.util.List;

public class ArticleInputModel {

    private int timestamp;
    private int articleCount;
    private List<ArticleModel> articleModels;

    public ArticleInputModel(int timestamp, int articleCount, List<ArticleModel> articleModels) {
        this.timestamp = timestamp;
        this.articleCount = articleCount;
        this.articleModels = articleModels;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public List<ArticleModel> getArticleModels() {
        return articleModels;
    }
}
