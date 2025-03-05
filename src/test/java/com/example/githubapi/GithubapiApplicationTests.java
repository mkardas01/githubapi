package com.example.githubapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class GithubapiApplicationTests {

	@Test
	void shouldReturnRepositoriesWithBranches() {
		given()
			.when()
				.get("/api/users/{username}/repositories", "octocat")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("$", not(empty()))
				.body("every { it.fork == false }", is(true))
				.body("every { it.name != null }", is(true))
				.body("every { it.owner.login == 'octocat' }", is(true))
				.body("every { it.branches != null }", is(true))
				.body("every { it.branches.every { branch -> branch.name != null } }", is(true))
				.body("every { it.branches.every { branch -> branch.commit.sha != null } }", is(true));
	}

	@Test
	void shouldReturn404ForNonExistingUser() {
		given()
			.when()
				.get("/api/users/{username}/repositories", "non-existing-user-123456789")
			.then()
				.statusCode(404)
				.contentType(ContentType.JSON)
				.body("status", is(404))
				.body("message", is("User not found"));
	}
}
