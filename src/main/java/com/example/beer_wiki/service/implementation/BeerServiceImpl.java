package com.example.beer_wiki.service.implementation;

import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.dto.BeerListDto;
import com.example.beer_wiki.model.Beer;
import com.example.beer_wiki.model.Brewery;
import com.example.beer_wiki.repository.BeerRepository;
import com.example.beer_wiki.repository.BreweryRepository;
import com.example.beer_wiki.service.BeerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository repository;
    private final BreweryRepository breweryRepository; // добавляем

    @Autowired
    private ModelMapper modelMapper;

    public BeerServiceImpl(BeerRepository repository,
                           BreweryRepository breweryRepository) {
        this.repository = repository;
        this.breweryRepository = breweryRepository;
    }

    @Override
    public Page<BeerDetailsDto> findAllPaginated(Pageable pageable) {
        Page<Beer> page = repository.findAll(pageable);
        return page.map(beer -> modelMapper.map(beer, BeerDetailsDto.class));
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
        Beer beer = new Beer();

        beer.setId(dto.getId());
        beer.setName(dto.getName());
        beer.setDescription(dto.getDescription());
        beer.setAbv(dto.getAbv());
        beer.setIbu(dto.getIbu());
        beer.setIngredients(dto.getIngredients() != null ? dto.getIngredients() : new ArrayList<>());

        if (dto.getBreweryName() != null && !dto.getBreweryName().isBlank()) {
            Brewery brewery = breweryRepository.findByName(dto.getBreweryName())
                    .orElseThrow(() -> new RuntimeException(
                            "Пивоварня с именем '" + dto.getBreweryName() + "' не найдена. " +
                            "Сначала добавьте пивоварню."));
            beer.setBrewery(brewery);
        } else {
            throw new RuntimeException("Название пивоварни обязательно для заполнения.");
        }
        return beer;
    }


}