package com.example.web_galliffetmandintexierfaho.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class Joueurs {
    @Id
    private Long jonumero;
    private String jonom;
    private String joprenom;
    private Date jodate_nai;
    private String joemail;
    private String joposte;
    private String jotelephone;


}