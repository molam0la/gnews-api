package com.molam0la.dev.gnews_api.controllers;

import com.molam0la.dev.gnews_api.services.GNewsArticleService;
import com.molam0la.dev.gnews_api.app_config.ConfigProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    private ConfigProps configProps;
    private GNewsArticleService GNewsArticleService;

    private static final Logger log = LoggerFactory.getLogger(ViewController.class);

    public ViewController(ConfigProps configProps, GNewsArticleService GNewsArticleService) {
        this.configProps = configProps;
        this.GNewsArticleService = GNewsArticleService;
    }

    @GetMapping("/")
    public String getArticles(@RequestParam(name="lang", required = false) String lang, Model model) {

        lang = (lang == null) ? configProps.getLang() : lang;

        configProps.setKeyword("hello");
        configProps.setLang(lang);

        model.addAttribute("articles", GNewsArticleService.createListOfArticles(GNewsArticleService.getArticlesBySearchWord()));
        model.addAttribute("keyword", configProps.getKeyword());
        model.addAttribute("lang", configProps.getLang());
        return "article";
    }

    @GetMapping("/topic/{topic}")
    public String getArticlesWithTopicUrl(@PathVariable ("topic") String topic,
                                          @RequestParam(name="lang", required = false) String lang, Model model) {

        lang = (lang == null) ? configProps.getLang() : lang;

        configProps.setTopic(topic);
        configProps.setLang(lang);

        model.addAttribute("articles", GNewsArticleService.createListOfArticles(GNewsArticleService.getArticlesByTopic()));
        model.addAttribute("topic", configProps.getTopic());
        model.addAttribute("lang", configProps.getLang());
        return "article";
    }

    @GetMapping("/search")
    public String getArticlesWithKeywordRequestParam(@RequestParam(name="keyword", required = false) String keyword,
                                                     @RequestParam(name="lang", required = false) String lang, Model model) {

        keyword = (keyword == null) ? configProps.getKeyword() : keyword;
        lang = (lang == null) ? configProps.getLang() : lang;

        configProps.setKeyword(keyword);
        configProps.setLang(lang);

        model.addAttribute("articles", GNewsArticleService.createListOfArticles(GNewsArticleService.getArticlesBySearchWord()));
        model.addAttribute("keyword", configProps.getKeyword());
        model.addAttribute("lang", configProps.getLang());
        return "article";
    }

}
