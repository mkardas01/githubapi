package com.example.githubapi.model;

import lombok.Data;
import java.util.List;

@Data
public class Repository {
    private String name;
    private Owner owner;
    private List<Branch> branches;
    private boolean fork;
} 