package com.example.web_galliffetmandintexierfaho.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entraineurs {
    private Long enNumero;
    private Long eqNumero;
    private String enNom;
    private String enPrenom;
    private Date enDateNai;
    private String enEmail;
    private String enTelephone;
}