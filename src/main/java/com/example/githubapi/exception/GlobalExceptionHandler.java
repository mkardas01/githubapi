package com.example.githubapi.exception;

import com.example.githubapi.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(WebClientResponseException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(WebClientResponseException.Forbidden ex) {
        return new ErrorResponse(403, extractGitHubErrorMessage(ex));
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(WebClientResponseException.NotFound ex) {
        return new ErrorResponse(404, "User not found");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        if (ex instanceof WebClientResponseException) {
            return new ErrorResponse(500, extractGitHubErrorMessage((WebClientResponseException) ex));
        }
        return new ErrorResponse(500, "Internal server error");
    }

    private String extractGitHubErrorMessage(WebClientResponseException ex) {
        try {
            GitHubErrorResponse error = objectMapper.readValue(ex.getResponseBodyAsString(), GitHubErrorResponse.class);
            return error.getMessage();
        } catch (Exception e) {
            return ex.getMessage();
        }
    }

    private static class GitHubErrorResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
} 