package com.example.web_galliffetmandintexierfaho.controller;

import com.example.web_galliffetmandintexierfaho.entity.Equipe;
import com.example.web_galliffetmandintexierfaho.service.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @GetMapping
    public List<Equipe> getAllEquipes() {
        return equipeService.getAllEquipes();
    }
}
