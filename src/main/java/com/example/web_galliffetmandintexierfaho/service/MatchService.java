package com.example.web_galliffetmandintexierfaho.service;

// MatchService.java

import com.example.web_galliffetmandintexierfaho.entity.Match;
import com.example.web_galliffetmandintexierfaho.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    // Récupérer tous les matchs
    public List<Match> getAllMatches() {
        return matchRepository.getAllMatches();
    }

    // Récupérer un match par ID
    public Match getMatchById(Long matchNumero) {
        return matchRepository.getMatchById(matchNumero);
    }

    // Ajouter un match
    public void addMatch(Match match) {
        matchRepository.addMatch(match);
    }

    // Mettre à jour un match
    public void updateMatch(Match match) {
        matchRepository.updateMatch(match);
    }

    // Supprimer un match
    public void deleteMatch(Long matchNumero) {
        matchRepository.deleteMatch(matchNumero);
    }
}
