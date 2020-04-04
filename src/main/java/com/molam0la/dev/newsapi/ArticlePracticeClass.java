package com.molam0la.dev.newsapi;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@Component
public class ArticlePracticeClass {

    @Autowired
    public ArticlePracticeClass(NewsApi1 newsApi1) {
        this.newsApi1 = newsApi1;
    }

    public ArticlePracticeClass(List<String> articleTitles) {
        this.articleTitles = articleTitles;
    }

    private NewsApi1 newsApi1;

    private DocumentContext input;

    private int timeStamp = 0;
    private int articleCount;
    private List<String> articleTitles;
    private List<String> articleUrls;
    private List<String> publishedTimestamp;
    private List<String> source;
    private static final Logger log = LoggerFactory.getLogger(ArticlePracticeClass.class);

    public void setAllValues() {
        input = newsApi1.makeRequest()
                .map(value -> JsonPath.parse(value))
                .block();

        setInput(input);
        setTimeStamp(input.read("$.timestamp"));
        setArticleCount(input.read("$.articleCount"));
        setArticleTitles(input.read("$.articles..title"));
        setArticleUrls(input.read("$.articles..url"));
        setPublishedTimestamp(input.read("$.articles..publishedAt"));
        setSource(input.read("$.articles..source"));
    }

    public void setInput(DocumentContext input) {
        this.input = input;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public void setArticleTitles(List<String> articleTitles) {
        this.articleTitles = articleTitles;
    }

    public void setArticleUrls(List<String> articleUrls) {
        this.articleUrls = articleUrls;
    }

    public void setPublishedTimestamp(List<String> publishedTimestamp) {
        this.publishedTimestamp = publishedTimestamp;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public int getTimeStamp() {
        setInput(input);
        timeStamp = input.read("$.timestamp");
        return timeStamp;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public List<String> getArticleTitles() {
        return articleTitles;
    }

    public List<String> getArticleUrls() {
        return articleUrls;
    }

    public List<String> getPublishedTimestamp() {
        return publishedTimestamp;
    }

    public List<String> getSource() {
        return source;
    }

}


//    public String getArticles() {
//
//        return newsApi.makeRequest()
//                .subscribe(value -> log.info("Received value:" + value),
//                        error -> error.printStackTrace(),
//                        () -> log.error("Completed without a value"));
//
//    }
