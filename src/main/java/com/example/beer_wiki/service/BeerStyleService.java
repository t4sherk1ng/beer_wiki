package com.example.beer_wiki.service;

import com.example.beer_wiki.dto.BeerStyleDto;

import java.util.List;

public interface BeerStyleService {
    List<BeerStyleDto> findAll();
    BeerStyleDto findById(Long id);
    List<BeerStyleDto> searchByName(String name);
    BeerStyleDto save(BeerStyleDto dto);
    BeerStyleDto update(Long id, BeerStyleDto dto);
    void deleteById(Long id);
}