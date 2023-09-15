package dev.ljcaliwan.cmbackend.handler;

import dev.ljcaliwan.cmbackend.controller.HttpResponse;
import dev.ljcaliwan.cmbackend.exception.DuplicateResourceException;
import dev.ljcaliwan.cmbackend.exception.RequestValidationException;
import dev.ljcaliwan.cmbackend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> resourceNotFound(ResourceNotFoundException exception) {
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<HttpResponse> duplicateException(DuplicateResourceException exception) {
        return createHttpResponse(CONFLICT, exception.getMessage());
    }
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<HttpResponse> duplicateException(RequestValidationException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(new Date(), httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
