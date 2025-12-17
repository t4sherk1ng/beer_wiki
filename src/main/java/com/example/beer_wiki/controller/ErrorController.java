package com.example.beer_wiki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorController {

    @GetMapping("/error")
    public String errorPage() {
        log.debug("Отображение страницы ошибки");
        return "error";
    }

}
