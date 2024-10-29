package com.example.web_galliffetmandintexierfaho.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    private Long trNumero;
    private Long eqNumero;
    private LocalDateTime trDateDeb;
    private LocalDateTime trDateFin;
    private List<Materiel> materielsUtilises;
}
