package com.example.beer_wiki.service;

import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.dto.BreweryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BreweryService {
    List<BreweryDto> findAll();
    BreweryDto findById(Long id);
    BreweryDto findByIdWithBeers(Long id);  // С загрузкой связанных пив
    List<BreweryDto> searchByName(String name);
    BreweryDto save(BreweryDto dto);
    BreweryDto update(Long id, BreweryDto dto);
    Page<BreweryDto> findAllPaginated(Pageable pageable);
    void deleteById(Long id);
}