package com.example.web_galliffetmandintexierfaho.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Materiel {
    private Long materielNumero;
    private String materielNom;
    private Integer nombreUtilise;
}
