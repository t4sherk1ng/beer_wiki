package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.ReviewDto;
import com.example.beer_wiki.model.Beer;
import com.example.beer_wiki.model.Review;
import com.example.beer_wiki.model.User;
import com.example.beer_wiki.repository.BeerRepository;
import com.example.beer_wiki.repository.ReviewRepository;
import com.example.beer_wiki.repository.UserRepository;
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
    private final BeerRepository beerRepository;
    private final UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;


    public ReviewServiceImpl(ReviewRepository repository,
                             BeerRepository beerRepository,
                             UserRepository userRepository) {
        this.repository = repository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> findByUsername(String username) {
        return repository.findByUserUsernameOrderByDateDesc(username)
                .stream()
                .map(r -> modelMapper.map(r, ReviewDto.class))
                .toList();
    }

    @Override
    @Transactional
    public ReviewDto addReview(Long beerId, String username, double rating, String comment) {
        Beer beer = beerRepository.findById(beerId)
                .orElseThrow(() -> new IllegalArgumentException("Beer not found: " + beerId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Проверка: уже оставил отзыв?
        if (repository.existsByUserIdAndBeerId(user.getId(), beer.getId())) {
            throw new IllegalStateException("You have already reviewed this beer!");
        }

        Review review = new Review();
        review.setBeer(beer);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);

        Review saved = repository.save(review);
        return modelMapper.map(saved, ReviewDto.class);
    }

    @Override
    public boolean existsByUserIdAndBeerId(Long userId, Long beerId) {
        return repository.existsByUserIdAndBeerId(userId, beerId);
    }

    private ReviewDto convertToDto(Review entity) {
        return modelMapper.map(entity, ReviewDto.class);
    }

    private Review convertToEntity(ReviewDto dto) {
        return modelMapper.map(dto, Review.class);
    }
}