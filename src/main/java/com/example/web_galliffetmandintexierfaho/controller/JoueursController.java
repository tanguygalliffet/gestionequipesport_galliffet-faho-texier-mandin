package com.example.web_galliffetmandintexierfaho.controller;

import com.example.web_galliffetmandintexierfaho.service.JoueursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/joueurs")
public class JoueursController {

    private final JoueursService joueursService;

    @Autowired
    public JoueursController(JoueursService joueursService) {
        this.joueursService = joueursService;
    }

    @GetMapping
    public List<Map<String, Object>> getAllJoueurs() {
        return joueursService.getAllJoueurs();
    }

    @GetMapping("/poste")
    public List<Map<String, Object>> getJoueursParPoste(@RequestParam String poste) {
        return joueursService.getJoueursParPoste(poste);
    }

    @GetMapping("/plus-de-20-ans")
    public List<Map<String, Object>> getJoueursPlusDe20Ans() {
        return joueursService.getJoueursPlusDe20Ans();
    }

    // New endpoints

    @PostMapping
    public List<Map<String, Object>> addJoueur(@RequestBody Joueur joueur) {
        return joueursService.addJoueur(
                joueur.getNom(),
                joueur.getPrenom(),
                joueur.getDateNaissance(),
                joueur.getEmail(),
                joueur.getPoste(),
                joueur.getTelephone()
        );
    }

    @PutMapping("/{numero}")
    public List<Map<String, Object>> updateJoueur(
            @PathVariable int numero,
            @RequestBody Joueur joueur
    ) {
        return joueursService.updateJoueur(
                numero,
                joueur.getNom(),
                joueur.getPrenom(),
                joueur.getDateNaissance(),
                joueur.getEmail(),
                joueur.getPoste(),
                joueur.getTelephone()
        );
    }

    @DeleteMapping("/{numero}")
    public List<Map<String, Object>> deleteJoueur(@PathVariable int numero) {
        return joueursService.deleteJoueur(numero);
    }

    // Joueur DTO class
    public static class Joueur {
        private String nom;
        private String prenom;
        private Date dateNaissance;
        private String email;
        private String poste;
        private String telephone;

        // Getters and Setters

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public Date getDateNaissance() {
            return dateNaissance;
        }

        public void setDateNaissance(Date dateNaissance) {
            this.dateNaissance = dateNaissance;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPoste() {
            return poste;
        }

        public void setPoste(String poste) {
            this.poste = poste;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
