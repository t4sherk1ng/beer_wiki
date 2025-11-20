package com.example.beer_wiki.controller;

import com.example.beer_wiki.service.BeerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("breweries")
public class BreweryController {
    private BeerService beerService;

    public BreweryController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/all")
    public String showAllBreweries(){

    }
}
