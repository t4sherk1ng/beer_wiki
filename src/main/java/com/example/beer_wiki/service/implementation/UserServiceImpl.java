package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.UserDto;
import com.example.beer_wiki.model.User;
import com.example.beer_wiki.repository.UserRepository;
import com.example.beer_wiki.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public UserDto save(UserDto dto) {
        User entity = convertToEntity(dto);
        User saved = repository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        modelMapper.map(dto, existing);
        User updated = repository.save(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    private User convertToEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }
}