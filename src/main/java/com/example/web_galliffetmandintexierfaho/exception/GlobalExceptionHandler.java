// src/main/java/com/example/web_galliffetmandintexierfaho/exception/GlobalExceptionHandler.java

package com.example.web_galliffetmandintexierfaho.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Gestionnaire global des exceptions pour capturer et gérer les erreurs de manière cohérente.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions de type ResourceNotFoundException.
     *
     * @param ex L'exception ResourceNotFoundException.
     * @return Réponse avec les détails de l'erreur et le statut HTTP 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Resource Not Found");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère toutes les autres exceptions.
     *
     * @param ex L'exception générale.
     * @return Réponse avec les détails de l'erreur et le statut HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalExceptionHandler(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Server Error");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/**
 * Classe pour représenter les détails de l'erreur.
 */
class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    // Constructeur
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    public String getDetails() {
        return details;
    }
}
