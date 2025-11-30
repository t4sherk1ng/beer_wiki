package com.example.beer_wiki;

import com.example.beer_wiki.model.Role;
import com.example.beer_wiki.model.User;
import com.example.beer_wiki.model.UserRoles;
import com.example.beer_wiki.repository.UserRepository;
import com.example.beer_wiki.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Инициализация начальных данных при запуске приложения.
 */

@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepository userRepository,
                UserRoleRepository userRoleRepository,
                PasswordEncoder passwordEncoder,
                @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            userRoleRepository.saveAll(List.of(
                    new Role(UserRoles.ADMIN),
                    new Role(UserRoles.MODER),
                    new Role(UserRoles.USER)
            ));
        } else {
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initModerator();
            initNormalUser();
        }
    }

    private void initAdmin() {
        var adminRole = userRoleRepository
                .findRoleByName(UserRoles.ADMIN)
                .orElseThrow();

        var adminUser = new User(
                "admin1",
                "admin@example.com",
                UserRoles.ADMIN,
                passwordEncoder.encode(defaultPassword)

        );
        userRepository.save(adminUser);
    }

    private void initModerator() {
        var moderatorRole = userRoleRepository
                .findRoleByName(UserRoles.MODER)
                .orElseThrow();

        var moderatorUser = new User(
                "moder1",
                "moderator@example.com",
                UserRoles.MODER,
                passwordEncoder.encode(defaultPassword)
        );
        userRepository.save(moderatorUser);
    }

    private void initNormalUser() {
        var userRole = userRoleRepository
                .findRoleByName(UserRoles.USER)
                .orElseThrow();

        var normalUser = new User(
                "user228",
                "user@example.com",
                UserRoles.USER,
                passwordEncoder.encode(defaultPassword)
        );
        userRepository.save(normalUser);
    }
}