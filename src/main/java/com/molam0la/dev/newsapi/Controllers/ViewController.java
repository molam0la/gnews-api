package com.molam0la.dev.newsapi.Controllers;

import com.molam0la.dev.newsapi.ArticleService;
import com.molam0la.dev.newsapi.ConfigProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @Autowired
    private ConfigProps configProps;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/topic")
    public String getTopic(@RequestParam(name="topic", required=false, defaultValue="technology") String topic, Model model) {
        model.addAttribute("topic", topic);
        return "topic";
    }

    @GetMapping("/article")
    public String getArticle(Model model) {
        model.addAttribute("articles", articleService.createListOfArticles());
        model.addAttribute("topic", configProps.getTopic());
        return "article";
    }

    @GetMapping("/article/{topic}")
    public String getArticleWithTopic(@PathVariable ("topic") String topic, Model model) {
        configProps.setTopic(topic);
        model.addAttribute("articles", articleService.createListOfArticles());
        model.addAttribute("topic", configProps.getTopic());
        return "article";
    }



}
