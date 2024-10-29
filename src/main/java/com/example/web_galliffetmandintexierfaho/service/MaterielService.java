package com.example.web_galliffetmandintexierfaho.service;

import com.example.web_galliffetmandintexierfaho.entity.Materiel;
import com.example.web_galliffetmandintexierfaho.repository.MaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterielService {

    @Autowired
    private MaterielRepository materielRepository;

    public List<Materiel> getMaterielParTraining(Long trNumero) {
        return materielRepository.getMaterielParTraining(trNumero);
    }
}
