package com.example.web_galliffetmandintexierfaho.service;

import com.example.web_galliffetmandintexierfaho.entity.Entraineurs;
import com.example.web_galliffetmandintexierfaho.repository.EntraineursRepository;
import com.example.web_galliffetmandintexierfaho.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntraineursService {

    @Autowired
    private EntraineursRepository entraineurRepository;

    /**
     * Récupère tous les entraîneurs.
     */
    public List<Entraineurs> getAllEntraineurs() {
        return entraineurRepository.getAllEntraineurs();
    }

    /**
     * Recherche des entraîneurs par nom.
     */
    public List<Entraineurs> getEntraineursByNom(String nom) {
        return entraineurRepository.getEntraineursByNom(nom);
    }

    /**
     * Récupère un entraîneur par son numéro.
     */
    public Entraineurs getEntraineurById(Long enNumero) {
        Entraineurs entraineur = entraineurRepository.getEntraineurById(enNumero);
        if (entraineur == null) {
            throw new ResourceNotFoundException("Entraîneur non trouvé avec le numéro : " + enNumero);
        }
        return entraineur;
    }

    /**
     * Ajoute un nouvel entraîneur.
     */
    public Entraineurs addEntraineur(Entraineurs entraineur) {
        return entraineurRepository.addEntraineur(entraineur);
    }

    /**
     * Met à jour un entraîneur existant.
     */
    public Entraineurs updateEntraineur(Entraineurs entraineur) {
        Entraineurs updatedEntraineur = entraineurRepository.updateEntraineur(entraineur);
        if (updatedEntraineur == null) {
            throw new ResourceNotFoundException("Entraîneur non trouvé pour mise à jour avec le numéro : " + entraineur.getEnNumero());
        }
        return updatedEntraineur;
    }

    /**
     * Supprime un entraîneur par numéro.
     */
    public void deleteEntraineur(Long enNumero) {
        entraineurRepository.deleteEntraineur(enNumero);
    }
}
