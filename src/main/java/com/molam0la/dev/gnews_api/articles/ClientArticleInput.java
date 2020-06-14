package com.molam0la.dev.gnews_api.articles;

import java.util.List;

public class ClientArticleInput {

    private int timestamp;
    private int articleCount;
    private List<ClientArticle> clientArticles;

    public ClientArticleInput(int timestamp, int articleCount, List<ClientArticle> clientArticles) {
        this.timestamp = timestamp;
        this.articleCount = articleCount;
        this.clientArticles = clientArticles;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public List<ClientArticle> getClientArticles() {
        return clientArticles;
    }
}
