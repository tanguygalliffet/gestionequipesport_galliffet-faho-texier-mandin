package com.example.web_galliffetmandintexierfaho.controller;


import com.example.web_galliffetmandintexierfaho.entity.Match;
import com.example.web_galliffetmandintexierfaho.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // 1. Récupérer tous les matchs
    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    // 2. Récupérer un match par ID
    @GetMapping("/{matchNumero}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long matchNumero) {
        Match match = matchService.getMatchById(matchNumero);
        if (match != null) {
            return ResponseEntity.ok(match);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Ajouter un match
    @PostMapping
    public ResponseEntity<Match> addMatch(@RequestBody Match match) {
        matchService.addMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(match);
    }

    // 4. Mettre à jour un match
    @PutMapping("/{matchNumero}")
    public ResponseEntity<Void> updateMatch(@PathVariable Long matchNumero, @RequestBody Match match) {
        match.setMatchNumero(matchNumero);
        matchService.updateMatch(match);
        return ResponseEntity.ok().build();
    }

    // 5. Supprimer un match
    @DeleteMapping("/{matchNumero}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchNumero) {
        matchService.deleteMatch(matchNumero);
        return ResponseEntity.ok().build();
    }
}
