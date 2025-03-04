package com.example.githubapi.service;

import com.example.githubapi.model.Branch;
import com.example.githubapi.model.Repository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubService {
    private final WebClient webClient;

    public Multi<Repository> getUserRepositories(String username) {
        Flux<Repository> repositoryFlux = webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Repository>>() {})
                .flatMapMany(Flux::fromIterable)
                .filter(repo -> !repo.isFork())
                .flatMap(this::enrichRepositoryWithBranches);

        return Multi.createFrom().publisher(repositoryFlux);
    }

    private Mono<Repository> enrichRepositoryWithBranches(Repository repository) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches",
                        repository.getOwner().getLogin(),
                        repository.getName())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Branch>>() {})
                .map(branches -> {
                    repository.setBranches(branches);
                    return repository;
                })
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException) {
                        log.error("GitHub API error: {}", 
                            ((WebClientResponseException) error).getResponseBodyAsString());
                    }
                });
    }
} 