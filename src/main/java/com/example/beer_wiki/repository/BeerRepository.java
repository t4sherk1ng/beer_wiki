package com.example.beer_wiki.repository;

import com.example.beer_wiki.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    List<Beer> findByNameContainingIgnoreCase(String name);

    List<Beer> findByStyleId(Long styleId);

    List<Beer> findByBreweryId(Long breweryId);

    @Query("SELECT b FROM Beer b LEFT JOIN FETCH b.brewery LEFT JOIN FETCH b.style LEFT JOIN FETCH b.reviews WHERE b.name = :name")
    Beer findByNameWithDetails(@Param("name") String beerName);

    @Query("SELECT b FROM Beer b LEFT JOIN FETCH b.brewery LEFT JOIN FETCH b.style LEFT JOIN FETCH b.reviews WHERE b.id = :id")
    Beer findByIdWithDetails(@Param("id") Long id);

    // Расчет среднего рейтинга для конкретного пива
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.beer.id = :beerId")
    Double getAverageRatingByBeerId(@Param("beerId") Long beerId);
}
