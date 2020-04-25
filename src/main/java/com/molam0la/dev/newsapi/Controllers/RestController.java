package com.molam0la.dev.newsapi.Controllers;

import com.molam0la.dev.newsapi.ArticleProperties.ArticleInput;
import com.molam0la.dev.newsapi.ArticleService;
import com.molam0la.dev.newsapi.config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private ConfigProps configProps;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @RequestMapping("/gnews")
    public Mono<ArticleInput> getGNews() {
        return articleService.retrieveAllArticles();
    }

    @RequestMapping(method = POST, value = "/article")
    public Mono<ArticleInput> getGnewsWithTopic(@RequestBody String topic) {
        configProps.setTopic(topic);
        return articleService.retrieveAllArticles();
    }

}
