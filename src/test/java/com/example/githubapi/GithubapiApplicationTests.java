package com.example.githubapi;

import com.example.githubapi.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubapiApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void shouldReturnRepositoriesWithBranches() {
		// given
		String username = "octocat";

		// when & then
		webTestClient.get()
				.uri("/api/users/{username}/repositories", username)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Repository.class)
				.value(repositories -> {
					assertThat(repositories)
							.isNotEmpty()
							.allSatisfy(repository -> {
								assertThat(repository.getName()).isNotBlank();
								assertThat(repository.getOwner().getLogin()).isEqualTo(username);
								assertThat(repository.isFork()).isFalse();
								assertThat(repository.getBranches())
										.isNotNull()
										.allSatisfy(branch -> {
											assertThat(branch.getName()).isNotBlank();
											assertThat(branch.getCommit().getSha()).isNotBlank();
										});
							});
				});
	}

	@Test
	void shouldReturn404ForNonExistingUser() {
		// given
		String username = "non-existing-user-123456789";

		// when & then
		webTestClient.get()
				.uri("/api/users/{username}/repositories", username)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.status").isEqualTo(404)
				.jsonPath("$.message").isEqualTo("User not found");
	}
}
