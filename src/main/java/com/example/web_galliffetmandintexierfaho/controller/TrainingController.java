package com.example.web_galliffetmandintexierfaho.controller;

import com.example.web_galliffetmandintexierfaho.entity.Training;
import com.example.web_galliffetmandintexierfaho.service.TrainingService;
import com.example.web_galliffetmandintexierfaho.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    @Autowired
    private TrainingService trainingService;

    /**
     * GET /api/trainings
     * Récupère tous les entraînements.
     */
    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        try {
            List<Training> trainings = trainingService.getAllTrainings();
            return new ResponseEntity<>(trainings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les entraînements", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/trainings/{trNumero}
     * Récupère un entraînement par son numéro.
     */
    @GetMapping("/{trNumero}")
    public ResponseEntity<?> getTrainingById(@PathVariable("trNumero") Long trNumero) {
        try {
            Training training = trainingService.getTrainingById(trNumero);
            if (training == null) {
                String message = "Entraînement non trouvé avec le numéro : " + trNumero;
                logger.warn(message);
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(training, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la récupération de l'entraînement avec le numéro : " + trNumero;
            logger.error(errorMessage, e);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /api/trainings
     * Ajoute un nouvel entraînement.
     */
    @PostMapping
    public ResponseEntity<?> addTraining(@RequestBody Training training) {
        try {
            Training addedTraining = trainingService.addTraining(training);
            return new ResponseEntity<>(addedTraining, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'entraînement", e);
            return new ResponseEntity<>("Erreur lors de l'ajout de l'entraînement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /api/trainings/{trNumero}
     * Met à jour un entraînement existant.
     */
    @PutMapping("/{trNumero}")
    public ResponseEntity<?> updateTraining(
            @PathVariable("trNumero") Long trNumero,
            @RequestBody Training training) {
        try {
            training.setTrNumero(trNumero);
            Training updatedTraining = trainingService.updateTraining(training);
            if (updatedTraining == null) {
                String message = "Entraînement non trouvé pour mise à jour avec le numéro : " + trNumero;
                logger.warn(message);
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedTraining, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la mise à jour de l'entraînement avec le numéro : " + trNumero;
            logger.error(errorMessage, e);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /api/trainings/{trNumero}
     * Supprime un entraînement par numéro.
     */
    @DeleteMapping("/{trNumero}")
    public ResponseEntity<?> deleteTraining(@PathVariable("trNumero") Long trNumero) {
        try {
            boolean deleted = trainingService.deleteTraining(trNumero);
            if (!deleted) {
                String message = "Entraînement non trouvé pour suppression avec le numéro : " + trNumero;
                logger.warn(message);
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la suppression de l'entraînement avec le numéro : " + trNumero;
            logger.error(errorMessage, e);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
