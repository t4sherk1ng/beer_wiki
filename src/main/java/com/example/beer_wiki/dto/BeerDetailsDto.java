package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BeerDetailsDto {
    private Long id;
    private String name;
    private String description;  // Описание
    private double abv;
    private int ibu;
    private String styleName;
    private String breweryName;
    private String breweryLocation;  // Локация пивоварни
    private List<String> ingredients;  // Ингредиенты (список)
    private double averageRating;
    private List<ReviewDto> reviews;  // Список отзывов

    public BeerDetailsDto() {}
}
