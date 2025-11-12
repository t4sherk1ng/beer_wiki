package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long id;
    private String userName;  // Имя пользователя
    private double rating;  // Рейтинг (1-5)
    private String comment;  // Комментарий
    private String date;  // Дата отзыва (как строка, e.g., "2023-10-01T12:00:00")

    // Конструкторы
    public ReviewDto() {}
}
