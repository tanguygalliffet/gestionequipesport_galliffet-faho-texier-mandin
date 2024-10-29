package com.example.web_galliffetmandintexierfaho.repository;

// MatchRepository.java

import com.example.web_galliffetmandintexierfaho.entity.Match;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class MatchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. Récupérer tous les matchs
    public List<Match> getAllMatches() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("GetTousLesMatchs")
                .declareParameters(
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR, new MatchRowMapper())
                );

        Map<String, Object> result = jdbcCall.execute();
        return (List<Match>) result.get("OUT_CURSOR");
    }

    // 2. Récupérer un match par ID
    public Match getMatchById(Long matchNumero) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("GetMatchById")
                .declareParameters(
                        new SqlParameter("P_MATCHNUMERO", Types.NUMERIC),
                        new SqlOutParameter("OUT_CURSOR", OracleTypes.CURSOR, new MatchRowMapper())
                );

        Map<String, Object> result = jdbcCall.execute(
                new MapSqlParameterSource("P_MATCHNUMERO", matchNumero)
        );

        List<Match> matches = (List<Match>) result.get("OUT_CURSOR");
        return matches.isEmpty() ? null : matches.get(0);
    }

    // 3. Ajouter un match
    public void addMatch(Match match) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("AddMatch")
                .declareParameters(
                        new SqlParameter("P_EQNUMERO", Types.NUMERIC),
                        new SqlParameter("P_MATCH_EQADV", Types.VARCHAR),
                        new SqlParameter("P_MATCHDATE", Types.TIMESTAMP),
                        new SqlParameter("P_MATCHLIEU", Types.VARCHAR),
                        new SqlParameter("P_MATCHEXTERIEUR", Types.NUMERIC),
                        new SqlParameter("P_MATCHBUT_ENC", Types.NUMERIC),
                        new SqlParameter("P_MATCHBUT_MIS", Types.NUMERIC),
                        new SqlOutParameter("OUT_MATCHNUMERO", Types.NUMERIC)
                );

        Map<String, Object> result = jdbcCall.execute(
                new MapSqlParameterSource()
                        .addValue("P_EQNUMERO", match.getEqNumero())
                        .addValue("P_MATCH_EQADV", match.getMatchEqAdv())
                        .addValue("P_MATCHDATE", Timestamp.valueOf(match.getMatchDate()))
                        .addValue("P_MATCHLIEU", match.getMatchLieu())
                        .addValue("P_MATCHEXTERIEUR", match.getMatchExterieur() ? 1 : 0)
                        .addValue("P_MATCHBUT_ENC", match.getMatchButEnc())
                        .addValue("P_MATCHBUT_MIS", match.getMatchButMis())
        );

        BigDecimal matchNumero = (BigDecimal) result.get("OUT_MATCHNUMERO");
        match.setMatchNumero(matchNumero.longValue());
    }

    // 4. Mettre à jour un match
    public void updateMatch(Match match) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("UpdateMatch")
                .declareParameters(
                        new SqlParameter("P_MATCHNUMERO", Types.NUMERIC),
                        new SqlParameter("P_EQNUMERO", Types.NUMERIC),
                        new SqlParameter("P_MATCH_EQADV", Types.VARCHAR),
                        new SqlParameter("P_MATCHDATE", Types.TIMESTAMP),
                        new SqlParameter("P_MATCHLIEU", Types.VARCHAR),
                        new SqlParameter("P_MATCHEXTERIEUR", Types.NUMERIC),
                        new SqlParameter("P_MATCHBUT_ENC", Types.NUMERIC),
                        new SqlParameter("P_MATCHBUT_MIS", Types.NUMERIC)
                );

        jdbcCall.execute(
                new MapSqlParameterSource()
                        .addValue("P_MATCHNUMERO", match.getMatchNumero())
                        .addValue("P_EQNUMERO", match.getEqNumero())
                        .addValue("P_MATCH_EQADV", match.getMatchEqAdv())
                        .addValue("P_MATCHDATE", Timestamp.valueOf(match.getMatchDate()))
                        .addValue("P_MATCHLIEU", match.getMatchLieu())
                        .addValue("P_MATCHEXTERIEUR", match.getMatchExterieur() ? 1 : 0)
                        .addValue("P_MATCHBUT_ENC", match.getMatchButEnc())
                        .addValue("P_MATCHBUT_MIS", match.getMatchButMis())
        );
    }

    // 5. Supprimer un match
    public void deleteMatch(Long matchNumero) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("GESTIONFOOTBALL")
                .withProcedureName("DeleteMatch")
                .declareParameters(
                        new SqlParameter("P_MATCHNUMERO", Types.NUMERIC)
                );

        jdbcCall.execute(
                new MapSqlParameterSource("P_MATCHNUMERO", matchNumero)
        );
    }

    // RowMapper pour mapper les résultats aux objets Match
    public static class MatchRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();
            match.setMatchNumero(rs.getLong("MATCHNUMERO"));
            match.setEqNumero(rs.getLong("EQNUMERO"));
            match.setMatchEqAdv(rs.getString("MATCH_EQADV"));
            match.setMatchDate(rs.getTimestamp("MATCHDATE").toLocalDateTime());
            match.setMatchLieu(rs.getString("MATCHLIEU"));
            match.setMatchExterieur(rs.getInt("MATCHEXTERIEUR") == 1);
            match.setMatchButEnc(rs.getInt("MATCHBUT_ENC"));
            match.setMatchButMis(rs.getInt("MATCHBUT_MIS"));
            match.setEqNom(rs.getString("EQNOM"));
            return match;
        }
    }
}
