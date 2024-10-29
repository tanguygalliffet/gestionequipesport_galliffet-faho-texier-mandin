package com.example.web_galliffetmandintexierfaho.service;

import com.example.web_galliffetmandintexierfaho.entity.Training;
import com.example.web_galliffetmandintexierfaho.repository.TrainingRepository;
import com.example.web_galliffetmandintexierfaho.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    /**
     * Récupère tous les entraînements.
     */
    public List<Training> getAllTrainings() {
        return trainingRepository.getAllTrainings();
    }

    /**
     * Récupère un entraînement par son numéro.
     */
    public Training getTrainingById(Long trNumero) {
        return trainingRepository.getTrainingById(trNumero);
    }

    /**
     * Ajoute un nouvel entraînement.
     */
    public Training addTraining(Training training) {
        return trainingRepository.addTraining(training);
    }

    /**
     * Met à jour un entraînement existant.
     */
    public Training updateTraining(Training training) {
        Training updatedTraining = trainingRepository.updateTraining(training);
        if (updatedTraining == null) {
            throw new ResourceNotFoundException("Entraînement non trouvé pour mise à jour avec le numéro : " + training.getTrNumero());
        }
        return updatedTraining;
    }

    /**
     * Supprime un entraînement par numéro.
     *
     * @return
     */
    public boolean deleteTraining(Long trNumero) {
        trainingRepository.deleteTraining(trNumero);
        return false;
    }
}
