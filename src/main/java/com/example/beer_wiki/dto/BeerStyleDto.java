package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeerStyleDto {
    private Long id;
    private String name;  // e.g., "IPA"
    private String description;

    public BeerStyleDto() {}
}
