package com.example.beer_wiki.service;

import java.util.List;
import com.example.beer_wiki.dto.ReviewDto;

public interface ReviewService {
    List<ReviewDto> findAll();
    ReviewDto findById(Long id);
    List<ReviewDto> findByBeerId(Long beerId);
    List<ReviewDto> findByUserId(Long userId);
    ReviewDto save(ReviewDto dto);
    ReviewDto update(Long id, ReviewDto dto);
    void deleteById(Long id);
    List<ReviewDto> findByUsername(String username);
    ReviewDto addReview(Long beerId, String username, double rating, String comment);
}