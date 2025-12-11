package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long id;
    private String userName;
    private double rating;
    private String comment;
    private String date;
    public Long beerId;
    public String beerName;

    public ReviewDto() {}
}
