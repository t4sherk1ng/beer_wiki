package com.example.beer_wiki.service;

import com.example.beer_wiki.dto.UserDto;
import com.example.beer_wiki.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
    Optional<UserDto> findByUsername(String username);
    UserDto save(UserDto dto);
    UserDto update(Long id, UserDto dto);
    void deleteById(Long id);
    UserDetails loadUserByUsername(String username);
    User register(String username, String email, String rawPassword);
    User findEntityByUsername(String username);

}