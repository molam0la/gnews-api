package com.molam0la.dev.newsapi.Controllers;

import com.molam0la.dev.newsapi.ArticleService;
import com.molam0la.dev.newsapi.ConfigProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @Autowired
    private ConfigProps configProps;

    @Autowired
    private ArticleService articleService;



    @GetMapping("/keyword")
    public String getKeyword(@RequestParam(name="keyword", required=false, defaultValue="technology") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "keyword";
    }

    @GetMapping("/article")
    public String getArticle(Model model) {
        model.addAttribute("articles", articleService.createListOfArticles());
        return "article";
    }

}
