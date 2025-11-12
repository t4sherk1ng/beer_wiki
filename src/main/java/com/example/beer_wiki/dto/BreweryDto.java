package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BreweryDto {
    private Long id;
    private String name;
    private String location;
    private String description;
    private List<BeerListDto> beers;

    public BreweryDto() {}
}
