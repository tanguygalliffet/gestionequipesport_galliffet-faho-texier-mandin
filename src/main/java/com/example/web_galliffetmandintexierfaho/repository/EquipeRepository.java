package com.example.web_galliffetmandintexierfaho.repository;

// EquipeRepository.java

import com.example.web_galliffetmandintexierfaho.entity.Equipe;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class EquipeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Equipe> getAllEquipes() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("GetToutesLesEquipes")
                .declareParameters(
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR, new EquipeRowMapper())
                );

        Map<String, Object> result = jdbcCall.execute();
        return (List<Equipe>) result.get("OUT_CURSOR");
    }

    // RowMapper pour mapper les résultats aux objets Equipe
    public static class EquipeRowMapper implements RowMapper<Equipe> {
        @Override
        public Equipe mapRow(ResultSet rs, int rowNum) throws SQLException {
            Equipe equipe = new Equipe();
            equipe.setEqNumero(rs.getLong("EQNUMERO"));
            equipe.setEqNom(rs.getString("EQNOM"));
            equipe.setEqNiveau(rs.getString("EQNIVEAU"));
            return equipe;
        }
    }
}
