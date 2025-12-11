package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.BeerStyleDto;
import com.example.beer_wiki.model.BeerStyle;
import com.example.beer_wiki.repository.BeerStyleRepository;
import com.example.beer_wiki.service.BeerStyleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerStyleServiceImpl implements BeerStyleService {

    private final BeerStyleRepository repository;
    @Autowired
    private ModelMapper modelMapper;


    public BeerStyleServiceImpl(BeerStyleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BeerStyleDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeerStyleDto findById(Long id) {
        return repository.findById((long) id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("BeerStyle not found with id: " + id));
    }

    @Override
    public List<BeerStyleDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BeerStyleDto save(BeerStyleDto dto) {
        BeerStyle entity = convertToEntity(dto);
        BeerStyle saved = repository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public BeerStyleDto update(Long id, BeerStyleDto dto) {
        BeerStyle existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("BeerStyle not found with id: " + id));
        modelMapper.map(dto, existing);
        BeerStyle updated = repository.save(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private BeerStyleDto convertToDto(BeerStyle entity) {
        return modelMapper.map(entity, BeerStyleDto.class);
    }

    private BeerStyle convertToEntity(BeerStyleDto dto) {
        return modelMapper.map(dto, BeerStyle.class);
    }
}
