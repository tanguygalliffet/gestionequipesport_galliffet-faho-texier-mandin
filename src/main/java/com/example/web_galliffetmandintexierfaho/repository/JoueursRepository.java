package com.example.web_galliffetmandintexierfaho.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JoueursRepository {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall getAllJoueursCall;
    private SimpleJdbcCall getJoueursParPosteCall;
    private SimpleJdbcCall getJoueursPlusDe20AnsCall;
    private SimpleJdbcCall addJoueurCall;
    private SimpleJdbcCall updateJoueurCall;
    private SimpleJdbcCall deleteJoueurCall;

    private static final Logger logger = LoggerFactory.getLogger(JoueursRepository.class);

    @Autowired
    public JoueursRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        // Initialize existing stored procedures
        this.getAllJoueursCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball") // Oracle package name
                .withProcedureName("GetTousLesJoueurs")
                .declareParameters(new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR));

        this.getJoueursParPosteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("GetJoueursParPoste")
                .declareParameters(
                        new SqlParameter("JOPOSTE_IN", Types.VARCHAR), // Updated parameter name
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                );

        this.getJoueursPlusDe20AnsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("GetJoueursPlusDe20Ans")
                .declareParameters(new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR));

        // Initialize new stored procedures
        this.addJoueurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("AddJoueur")
                .declareParameters(
                        new SqlParameter("JO_NOM", Types.VARCHAR),
                        new SqlParameter("JO_PRENOM", Types.VARCHAR),
                        new SqlParameter("JO_DATE_NAI", Types.DATE),
                        new SqlParameter("JO_EMAIL", Types.VARCHAR),
                        new SqlParameter("JO_POSTE", Types.VARCHAR),
                        new SqlParameter("JO_TELEPHONE", Types.VARCHAR),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                );

        this.updateJoueurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("UpdateJoueur")
                .declareParameters(
                        new SqlParameter("JO_NUMERO", Types.NUMERIC),
                        new SqlParameter("JO_NOM", Types.VARCHAR),
                        new SqlParameter("JO_PRENOM", Types.VARCHAR),
                        new SqlParameter("JO_DATE_NAI", Types.DATE),
                        new SqlParameter("JO_EMAIL", Types.VARCHAR),
                        new SqlParameter("JO_POSTE", Types.VARCHAR),
                        new SqlParameter("JO_TELEPHONE", Types.VARCHAR),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                );

        this.deleteJoueurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("DeleteJoueur")
                .declareParameters(
                        new SqlParameter("JO_NUMERO", Types.NUMERIC),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR)
                );
    }

    // Existing methods...

    public List<Map<String, Object>> getAllJoueurs() {
        Map<String, Object> result = getAllJoueursCall.execute();
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueursList = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueursList != null && !joueursList.isEmpty()) {
                return joueursList;
            } else {
                logger.error("No rows returned in OUT_CURSOR for GetTousLesJoueurs procedure");
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for GetTousLesJoueurs procedure");
        }
        return List.of(); // Return empty list instead of null
    }

    public List<Map<String, Object>> getJoueursParPoste(String poste) {
        Map<String, Object> result = getJoueursParPosteCall.execute(new MapSqlParameterSource("JOPOSTE_IN", poste));
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueursList = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueursList != null && !joueursList.isEmpty()) {
                return joueursList;
            } else {
                logger.error("No rows returned in OUT_CURSOR for GetJoueursParPoste procedure with poste: " + poste);
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for GetJoueursParPoste procedure with poste: " + poste);
        }
        return List.of(); // Return empty list instead of null
    }

    public List<Map<String, Object>> getJoueursPlusDe20Ans() {
        Map<String, Object> result = getJoueursPlusDe20AnsCall.execute();
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueursList = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueursList != null && !joueursList.isEmpty()) {
                return joueursList;
            } else {
                logger.error("No rows returned in OUT_CURSOR for GetJoueursPlusDe20Ans procedure");
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for GetJoueursPlusDe20Ans procedure");
        }
        return List.of(); // Return empty list instead of null
    }

    // New methods...

    public List<Map<String, Object>> addJoueur(String nom, String prenom, java.util.Date dateNaissance, String email, String poste, String telephone) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("JO_NOM", nom)
                .addValue("JO_PRENOM", prenom)
                .addValue("JO_DATE_NAI", new java.sql.Date(dateNaissance.getTime()))
                .addValue("JO_EMAIL", email)
                .addValue("JO_POSTE", poste)
                .addValue("JO_TELEPHONE", telephone);

        Map<String, Object> result = addJoueurCall.execute(params);
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueur = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueur != null && !joueur.isEmpty()) {
                return joueur;
            } else {
                logger.error("No rows returned in OUT_CURSOR for AddJoueur procedure");
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for AddJoueur procedure");
        }
        return List.of();
    }

    public List<Map<String, Object>> updateJoueur(int numero, String nom, String prenom, java.util.Date dateNaissance, String email, String poste, String telephone) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("JO_NUMERO", numero)
                .addValue("JO_NOM", nom)
                .addValue("JO_PRENOM", prenom)
                .addValue("JO_DATE_NAI", new java.sql.Date(dateNaissance.getTime()))
                .addValue("JO_EMAIL", email)
                .addValue("JO_POSTE", poste)
                .addValue("JO_TELEPHONE", telephone);

        Map<String, Object> result = updateJoueurCall.execute(params);
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueur = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueur != null && !joueur.isEmpty()) {
                return joueur;
            } else {
                logger.error("No rows returned in OUT_CURSOR for UpdateJoueur procedure with JO_NUMERO: " + numero);
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for UpdateJoueur procedure with JO_NUMERO: " + numero);
        }
        return List.of();
    }

    public List<Map<String, Object>> deleteJoueur(int numero) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("JO_NUMERO", numero);

        Map<String, Object> result = deleteJoueurCall.execute(params);
        if (result != null && result.containsKey("OUT_CURSOR")) {
            List<Map<String, Object>> joueursList = (List<Map<String, Object>>) result.get("OUT_CURSOR");
            if (joueursList != null && !joueursList.isEmpty()) {
                return joueursList;
            } else {
                logger.error("No rows returned in OUT_CURSOR for DeleteJoueur procedure with JO_NUMERO: " + numero);
            }
        } else {
            logger.error("OUT_CURSOR key missing or null for DeleteJoueur procedure with JO_NUMERO: " + numero);
        }
        return List.of();
    }

    public Optional<Object> findById(long l) {
        return Optional.empty();
    }
}
