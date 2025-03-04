package com.example.githubapi.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Owner {
    @JsonProperty("login")
    private String login;
} 