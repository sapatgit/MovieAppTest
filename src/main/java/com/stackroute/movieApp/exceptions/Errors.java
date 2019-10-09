package com.stackroute.movieApp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class Errors {
    private HttpStatus status;
    private String body;

    public Errors(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
