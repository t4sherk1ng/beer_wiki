package com.example.beer_wiki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/index")
    public String homePage() {
        log.debug("Отображение главной страницы");
        return "index";
    }

}
