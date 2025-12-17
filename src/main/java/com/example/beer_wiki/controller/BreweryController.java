package com.example.beer_wiki.controller;

import com.example.beer_wiki.dto.BreweryDto;
import com.example.beer_wiki.service.BreweryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/breweries")
public class BreweryController {

    private final BreweryService breweryService;

    public BreweryController(BreweryService breweryService) {
        log.debug("Инициализация BreweryController");
        this.breweryService = breweryService;
    }

    @GetMapping("/add")
    public String addBrewery(Model model) {
        log.debug("Отображение формы добавления пивоварни");
        if (!model.containsAttribute("breweryModel")) {
            model.addAttribute("breweryModel", new BreweryDto());
        }
        return "brewery-add";
    }

    @PostMapping("/add")
    public String createBrewery(@ModelAttribute("breweryModel") BreweryDto breweryModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("breweryModel", breweryModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.breweryModel",
                    bindingResult
            );
            return "redirect:/breweries/add";
        }

        breweryService.save(breweryModel);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Пивоварня '" + breweryModel.getName() + "' успешно добавлена!"
        );

        return "redirect:/breweries/all";
    }

    @GetMapping("/all")
    public String showAllBreweries(@RequestParam(required = false) String search,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "name") String sortBy,
                                   Model model) {
        log.debug("Отображение страницы всех пивоварен");
        List<BreweryDto> breweries;
        if (search != null && !search.trim().isEmpty()) {
            breweries = breweryService.searchByName(search);
            model.addAttribute("search", search);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<BreweryDto> breweryPage = breweryService.findAllPaginated(pageable);
//            breweries = breweryService.findAll();findAll
            model.addAttribute("breweries", breweryPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", breweryPage.getTotalPages());
            model.addAttribute("totalItems", breweryPage.getTotalElements());
        }

//        model.addAttribute("breweries", breweryPage);
        return "brewery-all";
    }

    @GetMapping("/{id}")
    public String breweryDetails(@PathVariable Long id, Model model) {
        log.debug("Отображение страницы пивоварни " + breweryService.findById(id).getName());
        BreweryDto brewery = breweryService.findByIdWithBeers(id);
        model.addAttribute("breweryDetails", brewery);
        return "brewery-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteBrewery(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        breweryService.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Пивоварня с ID " + id + " успешно удалена!"
        );
        return "redirect:/breweries/all";
    }
}
