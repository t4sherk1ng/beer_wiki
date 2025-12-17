package com.example.beer_wiki.controller;

import com.example.beer_wiki.service.BeerService;
import com.example.beer_wiki.service.BreweryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Slf4j
@Controller
public class HomeController {

    private final BeerService beerService;
    private final BreweryService breweryService;

    public HomeController(BeerService beerService, BreweryService breweryService) {
        log.debug("Инициализация HomeController");
        this.beerService = beerService;
        this.breweryService = breweryService;
    }

    @GetMapping({"/", "/index"})
    public String homePage(Model model) {
        model.addAttribute("beers", beerService.findAll().subList(0, 4));
        model.addAttribute("breweries", breweryService.findAll().subList(0, 4));
        log.debug("Отображение главной страницы");
        return "index";
    }

}
