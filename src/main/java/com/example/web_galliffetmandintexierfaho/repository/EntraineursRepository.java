package com.example.web_galliffetmandintexierfaho.repository;

import com.example.web_galliffetmandintexierfaho.entity.Entraineurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EntraineursRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall getTousLesEntraineursCall;
    private SimpleJdbcCall getEntraineursParNomCall;
    private SimpleJdbcCall addEntraineurCall;
    private SimpleJdbcCall updateEntraineurCall;
    private SimpleJdbcCall deleteEntraineurCall;

    // Mapper pour transformer les résultats du curseur en objets Entraineurs
    private final RowMapper<Entraineurs> entraineurRowMapper = (rs, rowNum) -> {
        Entraineurs entraineur = new Entraineurs();
        entraineur.setEnNumero(rs.getLong("ENNUMERO"));
        entraineur.setEqNumero(rs.getLong("EQNUMERO"));
        entraineur.setEnNom(rs.getString("ENNOM"));
        entraineur.setEnPrenom(rs.getString("ENPRENOM"));
        entraineur.setEnDateNai(rs.getDate("ENDATE_NAI"));
        entraineur.setEnEmail(rs.getString("ENEMAIL"));
        entraineur.setEnTelephone(rs.getString("ENTELEPHONE"));
        return entraineur;
    };

    @PostConstruct
    public void init() {
        // Initialisation des appels aux procédures stockées

        // 1. GetTousLesEntraineurs
        getTousLesEntraineursCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball") // Nom du package PL/SQL
                .withProcedureName("GetTousLesEntraineurs")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, entraineurRowMapper)
                );

        // 2. GetEntraineursParNom
        getEntraineursParNomCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("GetEntraineursParNom")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlParameter("EN_NOM_IN", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, entraineurRowMapper)
                );

        // 3. AddEntraineur
        addEntraineurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("AddEntraineur")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlParameter("EN_NOM", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_PRENOM", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_DATE_NAI", Types.DATE),
                        new org.springframework.jdbc.core.SqlParameter("EN_EMAIL", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_TELEPHONE", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EQNUMERO", Types.NUMERIC),
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, entraineurRowMapper)
                );

        // 4. UpdateEntraineur
        updateEntraineurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("UpdateEntraineur")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlParameter("EN_NUMERO", Types.NUMERIC),
                        new org.springframework.jdbc.core.SqlParameter("EN_NOM", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_PRENOM", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_DATE_NAI", Types.DATE),
                        new org.springframework.jdbc.core.SqlParameter("EN_EMAIL", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EN_TELEPHONE", Types.VARCHAR),
                        new org.springframework.jdbc.core.SqlParameter("EQNUMERO", Types.NUMERIC),
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, entraineurRowMapper)
                );

        // 5. DeleteEntraineur
        deleteEntraineurCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GestionFootball")
                .withProcedureName("DeleteEntraineur")
                .declareParameters(
                        new org.springframework.jdbc.core.SqlParameter("EN_NUMERO", Types.NUMERIC),
                        new org.springframework.jdbc.core.SqlOutParameter("OUT_CURSOR", Types.REF_CURSOR, entraineurRowMapper)
                );
    }

    /**
     * Récupère tous les entraîneurs.
     */
    public List<Entraineurs> getAllEntraineurs() {
        Map<String, Object> result = getTousLesEntraineursCall.execute();
        return (List<Entraineurs>) result.get("OUT_CURSOR");
    }

    /**
     * Recherche des entraîneurs par nom.
     */
    public List<Entraineurs> getEntraineursByNom(String nom) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("EN_NOM_IN", nom);
        Map<String, Object> result = getEntraineursParNomCall.execute(inParams);
        return (List<Entraineurs>) result.get("OUT_CURSOR");
    }

    /**
     * Récupère un entraîneur par son numéro.
     */
    public Entraineurs getEntraineurById(Long enNumero) {
        // Puisqu'il n'y a pas de procédure stockée pour obtenir un entraîneur par ID,
        // nous récupérons tous les entraîneurs et filtrons par ID en Java.
        List<Entraineurs> entraineurs = getAllEntraineurs();
        return entraineurs.stream()
                .filter(e -> e.getEnNumero().equals(enNumero))
                .findFirst()
                .orElse(null);
    }

    /**
     * Ajoute un nouvel entraîneur et retourne l'objet ajouté.
     */
    public Entraineurs addEntraineur(Entraineurs entraineur) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("EN_NOM", entraineur.getEnNom());
        inParams.put("EN_PRENOM", entraineur.getEnPrenom());
        inParams.put("EN_DATE_NAI", new java.sql.Date(entraineur.getEnDateNai().getTime()));
        inParams.put("EN_EMAIL", entraineur.getEnEmail());
        inParams.put("EN_TELEPHONE", entraineur.getEnTelephone());
        inParams.put("EQNUMERO", entraineur.getEqNumero());

        Map<String, Object> result = addEntraineurCall.execute(inParams);
        List<Entraineurs> addedEntraineurs = (List<Entraineurs>) result.get("OUT_CURSOR");
        return addedEntraineurs.isEmpty() ? null : addedEntraineurs.get(0);
    }

    /**
     * Met à jour un entraîneur existant et retourne l'objet mis à jour.
     */
    public Entraineurs updateEntraineur(Entraineurs entraineur) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("EN_NUMERO", entraineur.getEnNumero());
        inParams.put("EN_NOM", entraineur.getEnNom());
        inParams.put("EN_PRENOM", entraineur.getEnPrenom());
        inParams.put("EN_DATE_NAI", new java.sql.Date(entraineur.getEnDateNai().getTime()));
        inParams.put("EN_EMAIL", entraineur.getEnEmail());
        inParams.put("EN_TELEPHONE", entraineur.getEnTelephone());
        inParams.put("EQNUMERO", entraineur.getEqNumero());

        Map<String, Object> result = updateEntraineurCall.execute(inParams);
        List<Entraineurs> updatedEntraineurs = (List<Entraineurs>) result.get("OUT_CURSOR");
        return updatedEntraineurs.isEmpty() ? null : updatedEntraineurs.get(0);
    }

    /**
     * Supprime un entraîneur par numéro.
     */
    public void deleteEntraineur(Long enNumero) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("EN_NUMERO", enNumero);
        deleteEntraineurCall.execute(inParams);
    }
}
