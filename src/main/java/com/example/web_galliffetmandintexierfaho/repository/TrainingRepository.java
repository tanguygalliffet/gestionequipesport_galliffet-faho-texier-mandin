package com.example.web_galliffetmandintexierfaho.repository;

import com.example.web_galliffetmandintexierfaho.entity.Materiel;
import com.example.web_galliffetmandintexierfaho.entity.Training;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class TrainingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall getAllTrainingsCall;
    private SimpleJdbcCall getTrainingByIdCall;
    private SimpleJdbcCall addTrainingCall;
    private SimpleJdbcCall updateTrainingCall;
    private SimpleJdbcCall deleteTrainingCall;

    // Map pour stocker les entraînements lors du mapping
    private Map<Long, Training> trainingMap;

    @PostConstruct
    public void init() {
        // Initialisation des appels aux procédures stockées

        // 1. GetTousLesTrainings
        getAllTrainingsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("GetTousLesTrainings")
                .declareParameters(
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                )
                .returningResultSet("OUT_CURSOR", getAllTrainingsRowMapper);

        // 2. GetTrainingById
        getTrainingByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("GetTrainingById")
                .declareParameters(
                        new SqlParameter("P_TRNUMERO", Types.NUMERIC),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                )
                .returningResultSet("OUT_CURSOR", getTrainingByIdRowMapper);

        // 3. AddTraining
        addTrainingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("AddTraining")
                .declareParameters(
                        new SqlParameter("EQNUMERO", Types.NUMERIC),
                        new SqlParameter("TRDATE_DEB", Types.TIMESTAMP),
                        new SqlParameter("TRDATE_FIN", Types.TIMESTAMP),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                )
                .returningResultSet("OUT_CURSOR", getTrainingByIdRowMapper);

        // 4. UpdateTraining
        updateTrainingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("UpdateTraining")
                .declareParameters(
                        new SqlParameter("P_TRNUMERO", Types.NUMERIC),
                        new SqlParameter("P_EQNUMERO", Types.NUMERIC),
                        new SqlParameter("P_TRDATE_DEB", Types.TIMESTAMP),
                        new SqlParameter("P_TRDATE_FIN", Types.TIMESTAMP),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                )
                .returningResultSet("OUT_CURSOR", getTrainingByIdRowMapper);

        // 5. DeleteTraining
        deleteTrainingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("DeleteTraining")
                .declareParameters(
                        new SqlParameter("P_TRNUMERO", Types.NUMERIC)
                );
    }

    /**
     * Mapper pour GetTousLesTrainings, incluant les matériels associés
     */
    private RowMapper<Training> getAllTrainingsRowMapper = new RowMapper<Training>() {
        @Override
        public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long trNumero = rs.getLong("TRNUMERO");

            Training training = trainingMap.get(trNumero);
            if (training == null) {
                training = new Training();
                training.setTrNumero(trNumero);
                training.setEqNumero(rs.getLong("EQNUMERO"));
                training.setTrDateDeb(rs.getTimestamp("TRDATE_DEB").toLocalDateTime());
                training.setTrDateFin(rs.getTimestamp("TRDATE_FIN").toLocalDateTime());
                training.setMaterielsUtilises(new ArrayList<>());
                trainingMap.put(trNumero, training);
            }

            Long materielNumero = rs.getLong("MATERIELNUMERO");
            if (!rs.wasNull()) {
                Materiel materiel = new Materiel();
                materiel.setMaterielNumero(materielNumero);
                materiel.setMaterielNom(rs.getString("MATERIELNOM"));
                materiel.setNombreUtilise(rs.getInt("NOMBREUTILISE"));
                training.getMaterielsUtilises().add(materiel);
            }

            return training;
        }
    };


    /**
     * Mapper pour GetTrainingById, incluant les matériels associés
     */
    private RowMapper<Training> getTrainingByIdRowMapper = new RowMapper<Training>() {
        @Override
        public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long trNumero = rs.getLong("TRNUMERO");

            Training training = trainingMap.get(trNumero);
            if (training == null) {
                training = new Training();
                training.setTrNumero(trNumero);
                training.setEqNumero(rs.getLong("EQNUMERO"));
                training.setTrDateDeb(rs.getTimestamp("TRDATE_DEB").toLocalDateTime());
                training.setTrDateFin(rs.getTimestamp("TRDATE_FIN").toLocalDateTime());
                training.setMaterielsUtilises(new ArrayList<>());
                trainingMap.put(trNumero, training);
            }

            Long materielNumero = rs.getLong("MATERIELNUMERO");
            if (materielNumero != 0) {
                Materiel materiel = new Materiel();
                materiel.setMaterielNumero(materielNumero);
                materiel.setMaterielNom(rs.getString("MATERIELNOM"));
                materiel.setNombreUtilise(rs.getInt("NOMBREUTILISE"));
                training.getMaterielsUtilises().add(materiel);
            }

            return training;
        }
    };

    /**
     * Récupère tous les entraînements, incluant les matériels associés
     */
    public List<Training> getAllTrainings() {
        trainingMap = new LinkedHashMap<>(); // Utiliser LinkedHashMap pour préserver l'ordre

        getAllTrainingsCall.execute();

        return new ArrayList<>(trainingMap.values());
    }

    /**
     * Récupère un entraînement par son numéro, incluant les matériels associés
     */
    public Training getTrainingById(Long trNumero) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", trNumero);

        trainingMap = new HashMap<>();

        getTrainingByIdCall.execute(inParams);

        return trainingMap.get(trNumero);
    }

    /**
     * Ajoute un nouvel entraînement
     */
    public Training addTraining(Training training) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("EQNUMERO", training.getEqNumero());
        inParams.put("TRDATE_DEB", Timestamp.valueOf(training.getTrDateDeb()));
        inParams.put("TRDATE_FIN", Timestamp.valueOf(training.getTrDateFin()));

        trainingMap = new HashMap<>();

        Map<String, Object> out = addTrainingCall.execute(inParams);

        Training addedTraining = trainingMap.values().stream().findFirst().orElse(null);

        // Gérer les matériels associés
        if (addedTraining != null && training.getMaterielsUtilises() != null) {
            for (Materiel materiel : training.getMaterielsUtilises()) {
                addMaterielDansTraining(addedTraining.getTrNumero(), materiel);
            }
            // Recharger l'entraînement pour inclure les matériels ajoutés
            addedTraining = getTrainingById(addedTraining.getTrNumero());
        }

        return addedTraining;
    }

    /**
     * Met à jour un entraînement existant
     */
    public Training updateTraining(Training training) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", training.getTrNumero());
        inParams.put("P_EQNUMERO", training.getEqNumero());
        inParams.put("P_TRDATE_DEB", Timestamp.valueOf(training.getTrDateDeb()));
        inParams.put("P_TRDATE_FIN", Timestamp.valueOf(training.getTrDateFin()));

        trainingMap = new HashMap<>();

        Map<String, Object> out = updateTrainingCall.execute(inParams);

        Training updatedTraining = trainingMap.values().stream().findFirst().orElse(null);

        if (updatedTraining != null) {
            // Supprimer les matériels existants
            deleteAllMaterielsDansTraining(updatedTraining.getTrNumero());

            // Ajouter les nouveaux matériels
            if (training.getMaterielsUtilises() != null) {
                for (Materiel materiel : training.getMaterielsUtilises()) {
                    addMaterielDansTraining(updatedTraining.getTrNumero(), materiel);
                }
            }

            // Recharger l'entraînement pour inclure les matériels mis à jour
            updatedTraining = getTrainingById(updatedTraining.getTrNumero());
        }

        return updatedTraining;
    }

    /**
     * Supprime un entraînement par numéro
     */
    public void deleteTraining(Long trNumero) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", trNumero);

        // Supprimer les matériels associés
        deleteAllMaterielsDansTraining(trNumero);

        deleteTrainingCall.execute(inParams);
    }

    /**
     * Ajoute un matériel dans un entraînement
     */
    private void addMaterielDansTraining(Long trNumero, Materiel materiel) {
        SimpleJdbcCall addMaterielCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("AddMaterielDansTraining")
                .declareParameters(
                        new SqlParameter("P_TRNUMERO", Types.NUMERIC),
                        new SqlParameter("P_MATERIELNUMERO", Types.NUMERIC),
                        new SqlParameter("P_NOMBREUTILISE", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", trNumero);
        inParams.put("P_MATERIELNUMERO", materiel.getMaterielNumero());
        inParams.put("P_NOMBREUTILISE", materiel.getNombreUtilise());

        addMaterielCall.execute(inParams);
    }

    /**
     * Supprime tous les matériels associés à un entraînement
     */
    private void deleteAllMaterielsDansTraining(Long trNumero) {
        SimpleJdbcCall deleteMaterielsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("DeleteAllMaterielsDansTraining")
                .declareParameters(
                        new SqlParameter("P_TRNUMERO", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_TRNUMERO", trNumero);

        deleteMaterielsCall.execute(inParams);
    }
}
