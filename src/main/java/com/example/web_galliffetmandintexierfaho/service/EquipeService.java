package com.example.web_galliffetmandintexierfaho.service;

import com.example.web_galliffetmandintexierfaho.entity.Equipe;
import com.example.web_galliffetmandintexierfaho.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipesRepository;

    /**
     * Récupère toutes les équipes.
     */
    public List<Equipe> getAllEquipes() {
        return equipesRepository.getAllEquipes();
    }
}
