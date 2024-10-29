package com.example.web_galliffetmandintexierfaho.controller;

import com.example.web_galliffetmandintexierfaho.entity.Entraineurs;
import com.example.web_galliffetmandintexierfaho.service.EntraineursService;
import com.example.web_galliffetmandintexierfaho.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/entraineurs")
public class EntraineursController {

    @Autowired
    private EntraineursService entraineurService;

    /**
     * GET /api/entraineurs
     * Récupère tous les entraîneurs.
     */
    @GetMapping
    public ResponseEntity<List<Entraineurs>> getAllEntraineurs() {
        List<Entraineurs> entraineurs = entraineurService.getAllEntraineurs();
        return new ResponseEntity<>(entraineurs, HttpStatus.OK);
    }

    /**
     * GET /api/entraineurs/search?nom=...
     * Recherche des entraîneurs par nom.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Entraineurs>> getEntraineursByNom(@RequestParam("nom") String nom) {
        List<Entraineurs> entraineurs = entraineurService.getEntraineursByNom(nom);
        return new ResponseEntity<>(entraineurs, HttpStatus.OK);
    }

    /**
     * POST /api/entraineurs
     * Ajoute un nouvel entraîneur.
     */
    @PostMapping
    public ResponseEntity<Entraineurs> addEntraineur(@RequestBody Entraineurs entraineur) {
        Entraineurs addedEntraineur = entraineurService.addEntraineur(entraineur);
        return new ResponseEntity<>(addedEntraineur, HttpStatus.CREATED);
    }

    /**
     * GET /api/entraineurs/{enNumero}
     * Récupère un entraîneur par son numéro.
     */
    @GetMapping("/{enNumero}")
    public ResponseEntity<Entraineurs> getEntraineurById(@PathVariable Long enNumero) {
        Entraineurs entraineur = entraineurService.getEntraineurById(enNumero);
        return new ResponseEntity<>(entraineur, HttpStatus.OK);
    }

    /**
     * PUT /api/entraineurs/{enNumero}
     * Met à jour un entraîneur existant.
     */
    @PutMapping("/{enNumero}")
    public ResponseEntity<Entraineurs> updateEntraineur(
            @PathVariable("enNumero") Long enNumero,
            @RequestBody Entraineurs entraineur) {
        entraineur.setEnNumero(enNumero);
        Entraineurs updatedEntraineur = entraineurService.updateEntraineur(entraineur);
        return new ResponseEntity<>(updatedEntraineur, HttpStatus.OK);
    }

    /**
     * DELETE /api/entraineurs/{enNumero}
     * Supprime un entraîneur par numéro.
     */
    @DeleteMapping("/{enNumero}")
    public ResponseEntity<Void> deleteEntraineur(@PathVariable("enNumero") Long enNumero) {
        entraineurService.deleteEntraineur(enNumero);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gestion des exceptions personnalisées.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Gestion des exceptions génériques
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
