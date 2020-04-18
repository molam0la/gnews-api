package com.molam0la.dev.newsapi.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/keyword")
    public String getKeyword(String name, Model model) {
        model.addAttribute("name", name);
        return "keywordForm";
    }

}
