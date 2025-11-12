package com.example.beer_wiki.repository;

import com.example.beer_wiki.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    // Кастомный метод: поиск пива по части имени (для поиска)
    List<Beer> findByNameContainingIgnoreCase(String name);

    // Кастомный метод: поиск по стилю (по ID стиля)
    List<Beer> findByStyleId(Long styleId);

    // Кастомный метод: поиск по пивоварне (по ID пивоварни)
    List<Beer> findByBreweryId(Long breweryId);

    // Кастомный запрос: получение пива с eager-fetching связанных сущностей (brewery, style, reviews)
    @Query("SELECT b FROM Beer b LEFT JOIN FETCH b.brewery LEFT JOIN FETCH b.style LEFT JOIN FETCH b.reviews WHERE b.id = :id")
    Beer findByIdWithDetails(@Param("id") Long id);

    // Кастомный запрос: расчет среднего рейтинга для конкретного пива
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.beer.id = :beerId")
    Double getAverageRatingByBeerId(@Param("beerId") Long beerId);
}
