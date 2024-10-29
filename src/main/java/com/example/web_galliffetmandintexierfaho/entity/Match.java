package com.example.web_galliffetmandintexierfaho.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Match {
    private Long matchNumero;
    private Long eqNumero;
    private String matchEqAdv;
    private LocalDateTime matchDate;
    private String matchLieu;
    private Boolean matchExterieur;
    private Integer matchButEnc;
    private Integer matchButMis;
    private String eqNom;

}