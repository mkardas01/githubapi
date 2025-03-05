package com.example.githubapi.controller;

import com.example.githubapi.model.Repository;
import com.example.githubapi.service.GithubService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class GithubController {
    
    @Inject
    GithubService githubService;

    @GET
    @Path("/users/{username}/repositories")
    public Multi<Repository> getUserRepositories(@PathParam("username") String username) {
        log.info("Received request for user: {}", username);
        return githubService.getUserRepositories(username);
    }
} 