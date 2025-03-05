package com.example.githubapi.exception;

import com.example.githubapi.model.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ClientWebApplicationException webEx) {
            if (webEx.getResponse().getStatus() == 404) {
                return Response.status(404)
                        .entity(new ErrorResponse(404, "User not found"))
                        .build();
            }
            if (webEx.getResponse().getStatus() == 403) {
                return Response.status(403)
                        .entity(new ErrorResponse(403, webEx.getMessage()))
                        .build();
            }
        }
        return Response.status(500)
                .entity(new ErrorResponse(500, "Internal server error"))
                .build();
    }
}