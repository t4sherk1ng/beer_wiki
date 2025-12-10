package com.example.beer_wiki.controller;

import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.dto.ReviewDto;
import com.example.beer_wiki.service.BeerService;
import com.example.beer_wiki.service.BeerStyleService;
import com.example.beer_wiki.service.BreweryService;
import com.example.beer_wiki.service.ReviewService;
import com.example.beer_wiki.service.implementation.BeerStyleServiceImpl;
import com.example.beer_wiki.service.implementation.BreweryServiceImpl;
import com.example.beer_wiki.service.implementation.ReviewServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/beers")
public class BeerController {

    private final BeerService beerService;
    private final BeerStyleService beerStyleService;
    private final BreweryService breweryService;
    private final ReviewService reviewService;

    public BeerController(BeerService beerService, BeerStyleService beerStyleService, BreweryService breweryService, ReviewService reviewService) {
        this.beerService = beerService;
        this.beerStyleService = beerStyleService;
        this.breweryService = breweryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/add")
    public String addBeer(Model model) {
        if (!model.containsAttribute("beer")) {
            model.addAttribute("beer", new BeerDetailsDto());
        }

        model.addAttribute("styles", beerStyleService.findAll());
        model.addAttribute("breweries", breweryService.findAll());
        return "beer-add";
    }

    @PostMapping("/add")
    public String createBeer(@ModelAttribute("beerModel") BeerDetailsDto beerModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("beerModel", beerModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.beerModel",
                    bindingResult
            );
            return "redirect:/beers/add";
        }

        beerService.save(beerModel);
        redirectAttributes.addFlashAttribute("successMessage",
                "Пиво '" + beerModel.getName() + "' успешно добавлено!");

        return "redirect:/beers/all-beers";
    }

    @GetMapping("/all-beers")
    public String showAllBeers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("beers", beerService.searchByName(search));
            model.addAttribute("search", search);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<BeerDetailsDto> beerPage = beerService.findAllPaginated(pageable);

            model.addAttribute("beers", beerPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", beerPage.getTotalPages());
            model.addAttribute("totalItems", beerPage.getTotalElements());
        }

        return "beer-all";
    }

//    @GetMapping("/{name}")
//    public String beerDetails(@PathVariable String name, Model model) {
//        model.addAttribute("beerDetails", beerService.beerDetails(name));
//        return "beer-details";
//    }

    @GetMapping("/{id}")
    public String beerDetails(@PathVariable Long id, Model model) {
        BeerDetailsDto beer = beerService.findById(id);
        model.addAttribute("beerDetails", beer);

        // отзывы по этому пиву
        List<ReviewDto> reviews = reviewService.findByBeerId(id);
        model.addAttribute("userReviews", reviews);

        // объект формы для отзыва
        model.addAttribute("reviewForm", new ReviewDto());

        return "beer-details";
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public String addReview(@PathVariable Long id,
                            @ModelAttribute("reviewForm") ReviewDto reviewForm,
                            Principal principal) {
        if (principal == null) {
            return "redirect:/users/login";
        }

        String username = principal.getName();
        reviewService.addReview(id, username, reviewForm.getRating(), reviewForm.getComment());

        return "redirect:/beers/" + id + "#reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        beerService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Пиво с ID " + id + " успешно удалено!");
        return "redirect:/beers/all-beers";
    }
}
