package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Component
@Service
//service on app start up an instance of this class will be created
public class ArticleService {

    private GNews gNews;

    @Autowired
    public ArticleService(GNews gNews) {
        this.gNews = gNews;
    }

    public Mono<ArticleInput> createArticle() {
        return gNews.createWebClient()
                .get()
                .retrieve()
                .bodyToMono(ArticleInput.class);
    }
}
