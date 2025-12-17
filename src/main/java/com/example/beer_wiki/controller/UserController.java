package com.example.beer_wiki.controller;

import com.example.beer_wiki.dto.UserDto;
import com.example.beer_wiki.model.User;
import com.example.beer_wiki.service.ReviewService;
import com.example.beer_wiki.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
        log.info("UserController инициализирован");
    }

    @ModelAttribute("userRegistrationDto")
    public UserDto initForm() {
        return new UserDto();
    }

    @GetMapping("/register")
    public String register() {
        log.debug("Отображение формы регистрации");
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(UserDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }

        this.userService.register(userRegistrationDto.getUsername(), userRegistrationDto.getEmail(), userRegistrationDto.getRawPassword());

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Отображение формы логина");
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {
        log.debug("Ошибка логина");
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }
        log.debug("Отображение профиля");
        String username = principal.getName();

        userService.findByUsername(username)
                .ifPresent(userDto -> model.addAttribute("user", userDto));

        model.addAttribute("userReviews", reviewService.findByUsername(username));

        return "profile";
    }
}