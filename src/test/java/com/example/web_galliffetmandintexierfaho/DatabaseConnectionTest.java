package com.example.web_galliffetmandintexierfaho;


import com.example.web_galliffetmandintexierfaho.entity.Joueurs;
import com.example.web_galliffetmandintexierfaho.repository.JoueursRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JoueursRepository joueurRepository;

    @Test
    public void testDatabaseConnection() {
        Joueurs joueur = (Joueurs) joueurRepository.findById(1L).orElse(null);
        assertNotNull(joueur, "Database connection failed or no data found!");
    }
}