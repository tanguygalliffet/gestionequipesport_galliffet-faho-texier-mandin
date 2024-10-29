package com.example.web_galliffetmandintexierfaho.repository;

import com.example.web_galliffetmandintexierfaho.entity.Materiel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class MaterielRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall getMaterielsParTrainingCall;

    // Mapper pour transformer les résultats du curseur en objets Materiel
    private RowMapper<Materiel> materielRowMapper = new RowMapper<Materiel>() {
        @Override
        public Materiel mapRow(ResultSet rs, int rowNum) throws SQLException {
            Materiel materiel = new Materiel();
            materiel.setMaterielNumero(rs.getLong("MATERIELNUMERO"));
            materiel.setMaterielNom(rs.getString("MATERIELNOM"));
            materiel.setNombreUtilise(rs.getInt("NOMBREUTILISE"));
            // Si vous souhaitez inclure trNumero, assurez-vous qu'il est sélectionné dans la procédure
            return materiel;
        }
    };

    @PostConstruct
    public void init() {
        getMaterielsParTrainingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL") // Nom du package PL/SQL en majuscules
                .withProcedureName("GetMaterielsParTraining")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlParameter("P_TRNUMERO", Types.NUMERIC), // Utilisez 'P_TRNUMERO' en majuscules
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, materielRowMapper)
                );
    }

    public List<Materiel> getMaterielParTraining(Long trNumero) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", trNumero); // Utilisez 'P_TRNUMERO' en majuscules

        Map<String, Object> result = getMaterielsParTrainingCall.execute(inParams);
        return (List<Materiel>) result.get("OUT_CURSOR");
    }


}
