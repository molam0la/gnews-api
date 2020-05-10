package com.molam0la.dev.newsapi.Controllers;

import com.molam0la.dev.newsapi.ArticleService;
import com.molam0la.dev.newsapi.config.ConfigProps;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    private ConfigProps configProps;
    private ArticleService articleService;

    public ViewController(ConfigProps configProps, ArticleService articleService) {
        this.configProps = configProps;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String getArticles(Model model) {
        configProps.setKeyword("hello");
        model.addAttribute("articles", articleService.createListOfArticles(articleService.getArticlesBySearchWord()));
        model.addAttribute("keyword", configProps.getKeyword());
        return "article";
    }

    @GetMapping("/topic/{topic}")
    public String getArticlesWithTopicUrl(@PathVariable ("topic") String topic, Model model) {
        configProps.setTopic(topic);
        model.addAttribute("articles", articleService.createListOfArticles(articleService.getArticlesByTopic()));
        model.addAttribute("topic", configProps.getTopic());
        return "article";
    }

    @GetMapping("/topic")
    public String getArticlesWithTopicRequestParam(@RequestParam(name="topic", defaultValue = "technology") String topic, Model model) {
        configProps.setTopic(topic);
        model.addAttribute("articles", articleService.createListOfArticles(articleService.getArticlesByTopic()));
        model.addAttribute("topic", configProps.getTopic());
        return "article";
    }

    @GetMapping("/search/{keyword}")
    public String getArticlesWithKeywordUrl(@PathVariable("keyword") String keyword, Model model) {
        configProps.setKeyword(keyword);
        model.addAttribute("articles", articleService.createListOfArticles(articleService.getArticlesBySearchWord()));
        model.addAttribute("keyword", configProps.getKeyword());
        return "article";
    }

    @GetMapping("/search")
    public String getArticlesWithKeywordRequestParam(@RequestParam(name="keyword", defaultValue = "dog") String keyword, Model model) {
        configProps.setKeyword(keyword);
        model.addAttribute("articles", articleService.createListOfArticles(articleService.getArticlesBySearchWord()));
        model.addAttribute("keyword", configProps.getKeyword());
        return "article";
    }

}
