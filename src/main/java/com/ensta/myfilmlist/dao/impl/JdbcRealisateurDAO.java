package com.ensta.myfilmlist.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ensta.myfilmlist.dao.RealisateurDAO;
import com.ensta.myfilmlist.model.Realisateur;
import com.ensta.myfilmlist.persistence.ConnectionManager;

public class JdbcRealisateurDAO implements RealisateurDAO {

    private JdbcTemplate jdbcTemplate = ConnectionManager.getJdbcTemplate();

    private static final String FIND_ALL_QUERY = "SELECT id, nom, prenom, date_naissance FROM Realisateur";
    private static final String FIND_BY_ID_QUERY = "SELECT id, nom, prenom, date_naissance FROM Realisateur WHERE id = ?";
    private static final String FIND_BY_NOM_PRENOM_QUERY = "SELECT id, nom, prenom, date_naissance FROM Realisateur WHERE nom = ? AND prenom = ?";


    private RowMapper<Realisateur> realisateurMapper = (rs, rowNum) -> {
        Realisateur r = new Realisateur();
        r.setId(rs.getLong("id"));
        r.setNom(rs.getString("nom"));
        r.setPrenom(rs.getString("prenom"));
        r.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        return r;
    };

    @Override
    public List<Realisateur> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, realisateurMapper);
    }

    @Override
    public Realisateur findByNomAndPrenom(String nom, String prenom) {
        try {
            return jdbcTemplate.queryForObject(
                FIND_BY_NOM_PRENOM_QUERY,
                realisateurMapper,
                nom,
                prenom
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Optional<Realisateur> findById(long id) {
        try {
            Realisateur r = jdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                realisateurMapper,
                id
            );
            return Optional.of(r);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // ✔️ demandé par l’énoncé
        }
    }
}
