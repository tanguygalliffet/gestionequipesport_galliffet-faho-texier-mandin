package com.example.web_galliffetmandintexierfaho.controller;

import com.example.web_galliffetmandintexierfaho.entity.Materiel;
import com.example.web_galliffetmandintexierfaho.service.MaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materiel")
public class MaterielController {

    @Autowired
    private MaterielService materielService;

    @GetMapping("/parTraining/{trNumero}")
    public ResponseEntity<List<Materiel>> getMaterielParTraining(@PathVariable Long trNumero) {
        List<Materiel> materiels = materielService.getMaterielParTraining(trNumero);
        return ResponseEntity.ok(materiels);
    }
}
