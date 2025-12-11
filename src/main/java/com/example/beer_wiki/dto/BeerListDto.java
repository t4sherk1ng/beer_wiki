package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeerListDto {

    private Long id;
    private String name;
    private String styleName;
    private double abv;
    private int ibu;
    private String breweryName;
    private double averageRating;
    private String imageUrl;
    private String description;

    public BeerListDto() {}
}
