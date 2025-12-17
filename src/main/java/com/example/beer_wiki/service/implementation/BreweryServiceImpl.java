package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.BreweryDto;
import com.example.beer_wiki.repository.BreweryRepository;
import com.example.beer_wiki.service.BreweryService;
import com.example.beer_wiki.model.Brewery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreweryServiceImpl implements BreweryService {

    private final BreweryRepository repository;
    @Autowired
    private ModelMapper modelMapper;


    public BreweryServiceImpl(BreweryRepository repository) {
        this.repository = repository;

    }

    @Override
    @Cacheable(value = "breweries", key = "'all'")
    public List<BreweryDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BreweryDto findById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Brewery not found with id: " + id));
    }

    @Override
    public BreweryDto findByIdWithBeers(Long id) {
        Brewery entity = repository.findByIdWithBeers(id);
        if (entity == null) {
            throw new RuntimeException("Brewery not found with id: " + id);
        }
        return convertToDto(entity);
    }

    @Override
    public List<BreweryDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "breweries", allEntries = true)
    public BreweryDto save(BreweryDto dto) {
        Brewery entity = convertToEntity(dto);
        Brewery saved = repository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public BreweryDto update(Long id, BreweryDto dto) {
        Brewery existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brewery not found with id: " + id));
        modelMapper.map(dto, existing);
        Brewery updated = repository.save(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private BreweryDto convertToDto(Brewery entity) {
        return modelMapper.map(entity, BreweryDto.class);
    }

    private Brewery convertToEntity(BreweryDto dto) {
        return modelMapper.map(dto, Brewery.class);
    }
}