package com.example.githubapi.config;

import com.example.githubapi.model.Branch;
import com.example.githubapi.model.Repository;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@Path("/")
@RegisterRestClient(configKey = "github-api")
@Produces(MediaType.APPLICATION_JSON)
public interface GitHubClient {
    
    @GET
    @Path("/users/{username}/repos")
    Uni<List<Repository>> getRepositories(@PathParam("username") String username);
    
    @GET
    @Path("/repos/{owner}/{repo}/branches")
    Uni<List<Branch>> getBranches(@PathParam("owner") String owner, @PathParam("repo") String repo);
} 