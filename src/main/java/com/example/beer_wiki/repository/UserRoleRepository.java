package com.example.beer_wiki.repository;

import com.example.beer_wiki.model.Role;
import com.example.beer_wiki.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(UserRoles role);
}
