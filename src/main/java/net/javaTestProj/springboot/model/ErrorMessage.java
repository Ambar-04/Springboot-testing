package net.javaTestProj.springboot.model;

import org.springframework.http.HttpStatus;

public class ErrorMessage {

    private final HttpStatus status;
    private final String message;


    public ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
