package com.riu.hotelsearch.adapter.in.rest;

import com.riu.hotelsearch.adapter.in.rest.dto.ErrorResponseDto;
import com.riu.hotelsearch.application.exception.SearchNotFoundException;
import com.riu.hotelsearch.domain.exception.InvalidSearchException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(
                new ErrorResponseDto(Instant.now(), 400, "Validation error", details)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        return ResponseEntity.badRequest().body(
                new ErrorResponseDto(Instant.now(), 400, "Validation error", details)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponseDto(
                        Instant.now(),
                        400,
                        "Malformed request",
                        List.of("Request body is invalid or contains malformed date values")
                )
        );
    }

    @ExceptionHandler(InvalidSearchException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidSearch(InvalidSearchException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponseDto(
                        Instant.now(),
                        400,
                        "Bad request",
                        List.of(ex.getMessage())
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto(
                        Instant.now(),
                        500,
                        "Internal error",
                        List.of("Unexpected error")
                )
        );
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(SearchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(
                        Instant.now(),
                        404,
                        "Not found",
                        List.of(ex.getMessage())
                )
        );
    }
}