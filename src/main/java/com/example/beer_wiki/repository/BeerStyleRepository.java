package com.example.beer_wiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.beer_wiki.model.BeerStyle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerStyleRepository extends JpaRepository<BeerStyle, Long> {
    List<BeerStyle> findByNameContainingIgnoreCase(String name);
}
