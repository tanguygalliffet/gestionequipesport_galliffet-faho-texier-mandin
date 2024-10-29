package com.example.web_galliffetmandintexierfaho.service;

import com.example.web_galliffetmandintexierfaho.repository.JoueursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JoueursService {

    @Autowired
    private JoueursRepository joueursRepository;

    public List<Map<String, Object>> getAllJoueurs() {
        return joueursRepository.getAllJoueurs();
    }

    public List<Map<String, Object>> getJoueursParPoste(String poste) {
        return joueursRepository.getJoueursParPoste(poste);
    }

    public List<Map<String, Object>> getJoueursPlusDe20Ans() {
        return joueursRepository.getJoueursPlusDe20Ans();
    }

    // New service methods

    public List<Map<String, Object>> addJoueur(String nom, String prenom, Date dateNaissance, String email, String poste, String telephone) {
        return joueursRepository.addJoueur(nom, prenom, dateNaissance, email, poste, telephone);
    }

    public List<Map<String, Object>> updateJoueur(int numero, String nom, String prenom, Date dateNaissance, String email, String poste, String telephone) {
        return joueursRepository.updateJoueur(numero, nom, prenom, dateNaissance, email, poste, telephone);
    }

    public List<Map<String, Object>> deleteJoueur(int numero) {
        return joueursRepository.deleteJoueur(numero);
    }
}
