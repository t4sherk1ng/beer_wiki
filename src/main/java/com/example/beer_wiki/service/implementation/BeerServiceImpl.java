package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.dto.BeerListDto;
import com.example.beer_wiki.model.Beer;
import com.example.beer_wiki.repository.BeerRepository;
import com.example.beer_wiki.service.BeerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BeerListDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeerDetailsDto findById(Long id) {
        Beer entity = repository.findByIdWithDetails(id);
        if (entity == null) {
            throw new RuntimeException("Beer not found with id: " + id);
        }
        BeerDetailsDto dto = convertToDetailsDto(entity);
        dto.setAverageRating(getAverageRating(id));  // Добавляем расчет
        return dto;
    }

    @Override
    public List<BeerListDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeerListDto> findByStyleId(Long styleId) {
        return repository.findByStyleId(styleId).stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeerListDto> findByBreweryId(Long breweryId) {
        return repository.findByBreweryId(breweryId).stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BeerDetailsDto save(BeerDetailsDto dto) {
        Beer entity = convertToEntity(dto);
        Beer saved = repository.save(entity);
        return convertToDetailsDto(saved);
    }

    @Override
    @Transactional
    public BeerDetailsDto update(Long id, BeerDetailsDto dto) {
        Beer existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beer not found with id: " + id));
        modelMapper.map(dto, existing);
        Beer updated = repository.save(existing);
        return convertToDetailsDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public double getAverageRating(Long beerId) {
        Double avg = repository.getAverageRatingByBeerId(beerId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public BeerDetailsDto beerDetails(String beerName) {
        Beer entity = repository.findByNameWithDetails(beerName);
        if (entity == null) {
            throw new RuntimeException("Beer not found with name: " + beerName);
        }
        BeerDetailsDto dto = convertToDetailsDto(entity);
//        dto.setAverageRating(getAverageRating());  // Добавляем расчет
        return dto;
    }

    private BeerListDto convertToListDto(Beer entity) {
        return modelMapper.map(entity, BeerListDto.class);
    }

    private BeerDetailsDto convertToDetailsDto(Beer entity) {
        return modelMapper.map(entity, BeerDetailsDto.class);
    }

    private Beer convertToEntity(BeerDetailsDto dto) {
        return modelMapper.map(dto, Beer.class);
    }
}