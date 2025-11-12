package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.ReviewDto;
import com.example.beer_wiki.model.Review;
import com.example.beer_wiki.repository.ReviewRepository;
import com.example.beer_wiki.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    @Autowired
    private ModelMapper modelMapper;


    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReviewDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto findById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    public List<ReviewDto> findByBeerId(Long beerId) {
        return repository.findByBeerIdWithUser(beerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDto save(ReviewDto dto) {
        Review entity = convertToEntity(dto);
        Review saved = repository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public ReviewDto update(Long id, ReviewDto dto) {
        Review existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        modelMapper.map(dto, existing);
        Review updated = repository.save(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private ReviewDto convertToDto(Review entity) {
        return modelMapper.map(entity, ReviewDto.class);
    }

    private Review convertToEntity(ReviewDto dto) {
        return modelMapper.map(dto, Review.class);
    }
}