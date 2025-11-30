package com.example.beer_wiki.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String rawPassword;

    public UserDto() {}
}
