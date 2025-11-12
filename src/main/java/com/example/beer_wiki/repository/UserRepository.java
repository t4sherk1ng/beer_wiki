package com.example.beer_wiki.repository;

import com.example.beer_wiki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Кастомный метод: поиск пользователя по username (для аутентификации или уникальности)
    Optional<User> findByUsername(String username);

    // Кастомный метод: поиск по email
    Optional<User> findByEmail(String email);
}
