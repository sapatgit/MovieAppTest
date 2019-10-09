package com.stackroute.movieApp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler({ MovieAlreadyExistsException.class, MovieNotFoundException.class })
    public final ResponseEntity<Errors> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof MovieNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            MovieNotFoundException mnfe = (MovieNotFoundException) ex;

            return handleMovieNotFoundException(mnfe, headers, status, request);
        } else if (ex instanceof MovieAlreadyExistsException) {
            HttpStatus status = HttpStatus.CONFLICT;
            MovieAlreadyExistsException maee = (MovieAlreadyExistsException) ex;

            return handleMovieAlreadyExistsException(maee, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    public ResponseEntity<Errors> handleMovieNotFoundException(MovieNotFoundException mnfe, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(mnfe, new Errors(status, mnfe.getMessage()), headers, status, request);
    }

    public ResponseEntity<Errors> handleMovieAlreadyExistsException(MovieAlreadyExistsException maee, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(maee, new Errors(status, maee.getMessage()), headers, status, request);
    }

    public ResponseEntity<Errors> handleExceptionInternal(Exception ex, Errors body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<Errors>(body, headers, status);
    }
}
