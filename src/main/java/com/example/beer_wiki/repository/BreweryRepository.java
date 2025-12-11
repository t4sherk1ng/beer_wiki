package com.example.beer_wiki.repository;

import com.example.beer_wiki.model.Brewery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BreweryRepository extends JpaRepository<Brewery, Long> {
    List<Brewery> findByNameContainingIgnoreCase(String name);

    @Query("SELECT br FROM Brewery br LEFT JOIN FETCH br.beers WHERE br.id = :id")
    Brewery findByIdWithBeers(@Param("id") Long id);

    Optional<Brewery> findByName(String name);
}
