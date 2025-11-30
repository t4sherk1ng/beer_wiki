package com.example.beer_wiki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.beer_wiki.model.BeerStyle;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeerStyleRepository extends JpaRepository<BeerStyle, Long> {
    List<BeerStyle> findByNameContainingIgnoreCase(String name);

    Optional<BeerStyle> findByName(String name);
}
