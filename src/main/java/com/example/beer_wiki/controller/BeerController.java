package com.example.beer_wiki.controller;


import com.example.beer_wiki.dto.BeerDetailsDto;
import com.example.beer_wiki.service.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/beers")
public class BeerController {

    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/add")
    public String addBeer(){
        return "beer-add";
    }

    @GetMapping("/all-beers")
    public String showAllBeers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("companyInfos", beerService.searchByName(search));
            model.addAttribute("search", search);
        } else {
//            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
////            Page<ShowCompanyInfoDto> companyPage = beerService.allCompaniesPaginated(pageable);
//
//            model.addAttribute("companyInfos", companyPage.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", companyPage.getTotalPages());
//            model.addAttribute("totalItems", companyPage.getTotalElements());
        }

        return "beer-all";
    }

    @PostMapping("/add")
    public String addBeer(BeerDetailsDto beerModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("beerModel", beerModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.companyModel",
                    bindingResult);
            return "redirect:/beers/add";
        }

        beerService.save(beerModel);
        redirectAttributes.addFlashAttribute("successMessage",
                "Пиво '" + beerModel.getName() + "' успешно добавлено!");

        return "redirect:/companies/all";
    }


    @GetMapping("/{name}")
    public String beerDetails(@PathVariable("name") String name, Model model){
        model.addAttribute("beerDetails", beerService.beerDetails(name));
        return "beer-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteBeer(@PathVariable("id")Long id, RedirectAttributes redirectAttributes) {
        beerService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Пиво '" + id + "' успешно удалено!");
        return "redirect:/beers/all";
    }
}
