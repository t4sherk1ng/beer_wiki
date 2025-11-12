package com.example.beer_wiki.service;

import com.example.beer_wiki.dto.BreweryDto;

import java.util.List;

public interface BreweryService {
    List<BreweryDto> findAll();
    BreweryDto findById(Long id);
    BreweryDto findByIdWithBeers(Long id);  // С загрузкой связанных пив
    List<BreweryDto> searchByName(String name);
    BreweryDto save(BreweryDto dto);
    BreweryDto update(Long id, BreweryDto dto);
    void deleteById(Long id);
}