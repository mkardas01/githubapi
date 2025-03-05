package com.example.githubapi.service;

import com.example.githubapi.config.GitHubClient;
import com.example.githubapi.model.Repository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Slf4j
public class GithubService {
    
    @Inject
    @RestClient
    GitHubClient githubClient;

    public Multi<Repository> getUserRepositories(String username) {
        return githubClient.getRepositories(username)
                .onItem().transformToMulti(repos -> Multi.createFrom().iterable(repos))
                .filter(repo -> !repo.isFork())
                .onItem().transformToUniAndMerge(this::enrichWithBranches);
    }

    private Uni<Repository> enrichWithBranches(Repository repo) {
        return githubClient.getBranches(repo.getOwner().getLogin(), repo.getName())
                .map(branches -> {
                    repo.setBranches(branches);
                    return repo;
                });
    }
} 