package com.example.githubapi.controller;

import com.example.githubapi.model.Repository;
import com.example.githubapi.service.GithubService;
import io.smallrye.mutiny.Multi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class GithubController {
    private final GithubService githubService;

    @GetMapping("/users/{username}/repositories")
    public Multi<Repository> getUserRepositories(@PathVariable String username) {
        log.info("Received request for user: {}", username);
        return githubService.getUserRepositories(username);
    }
} 