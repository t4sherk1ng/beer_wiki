package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeerListDto {

    private Long id;
    private String name;  // Название сорта
    private String styleName;  // Название стиля (e.g., IPA)
    private double abv;  // Алкогольный объем
    private int ibu;  // Горечь
    private String breweryName;  // Название пивоварни
    private double averageRating;

    public BeerListDto() {}
}
