package com.example.beer_wiki.config;

import com.example.beer_wiki.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AppSecurityConfiguration {

    // REMOVE the constructor-injected UserDetailsService field
    // private final UserDetailsService userDetailsService;
    //
    // public AppSecurityConfiguration(UserDetailsService userDetailsService) {
    //     this.userDetailsService = userDetailsService;
    // }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // TODO: Adapt this to your actual User entity / UserDetails implementation
        return username -> {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        // If your User implements UserDetails, just cast:
                        // return (UserDetails) user;

                        // Otherwise, build a Spring Security user from your entity:
                        return org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole().name()) // adjust according to your model
                                .build();
                    })
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User %s not found".formatted(username))
                    );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityContextRepository securityContextRepository,
                                                   UserDetailsService userDetailsService) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/img/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/favicon.ico", "/error").permitAll()
                        .requestMatchers("/", "/users/login", "/users/register", "/users/login-error").permitAll()
                        .requestMatchers("/actuator/**").authenticated()
                        .requestMatchers("/beers/all-beers", "/breweries/all", "/beers/{id}", "/breweries/{id}").permitAll()
                        .requestMatchers("/beers/add", "/beers/delete/**", "/breweries/add", "/breweries/delete/**").authenticated()
                        .requestMatchers("/users/profile").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login")
                        .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                        .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                        .defaultSuccessUrl("/", true)
                        .failureForwardUrl("/error")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400 * 7)
                        .userDetailsService(userDetailsService)
                        .rememberMeParameter("remember-me")
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(securityContextRepository)
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/actuator/**")
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}