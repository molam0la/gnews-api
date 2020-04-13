package com.molam0la.dev.newsapi;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @RequestMapping("/gnews")
    public Mono<ArticleInput> getGNews() {
        return articleService.retrieveAllArticles();
    }

    @GetMapping("/hi")
    public String sayHi() {
        return "HIIII";
    }

}
