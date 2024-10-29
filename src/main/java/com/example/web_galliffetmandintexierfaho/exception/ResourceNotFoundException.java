
package com.example.web_galliffetmandintexierfaho.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée pour les ressources non trouvées.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur avec message.
     *
     * @param message Message d'erreur.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
