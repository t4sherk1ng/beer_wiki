package com.example.beer_wiki.repository;


import com.example.beer_wiki.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Кастомный метод: все отзывы для конкретного пива
    List<Review> findByBeerId(Long beerId);

    // Кастомный метод: все отзывы от конкретного пользователя
    List<Review> findByUserId(Long userId);

    // Кастомный запрос: отзывы для пива с eager-fetching пользователя (чтобы загрузить user сразу)
    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.beer.id = :beerId")
    List<Review> findByBeerIdWithUser(@Param("beerId") Long beerId);

    List<Review> findByBeerIdOrderByDateDesc(Long beerId);

    List<Review> findByUserUsernameOrderByDateDesc(String username);
}
