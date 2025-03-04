package com.example.githubapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GithubapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubapiApplication.class, args);
	}

}
