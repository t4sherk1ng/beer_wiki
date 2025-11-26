package com.example.beer_wiki.service;

import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.dto.BeerListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BeerService {
    List<BeerListDto> findAll();
    BeerDetailsDto findById(Long id);
    List<BeerListDto> searchByName(String name);
    List<BeerListDto> findByStyleId(Long styleId);
    List<BeerListDto> findByBreweryId(Long breweryId);
    BeerDetailsDto save(BeerDetailsDto dto);
    BeerDetailsDto update(Long id, BeerDetailsDto dto);
    void deleteById(Long id);
    double getAverageRating(Long beerId);
    Page<BeerDetailsDto> findAllPaginated(Pageable pageable);

    BeerDetailsDto beerDetails(String beerName);
}