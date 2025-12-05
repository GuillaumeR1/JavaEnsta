package com.ensta.myfilmlist.dao;

import java.util.List;
import java.util.Optional;

import com.ensta.myfilmlist.model.Realisateur;

public interface RealisateurDAO {

    List<Realisateur> findAll();
    Optional<Realisateur> findById(long id);
    Realisateur findByNomAndPrenom(String nom, String prenom);
    Realisateur update(Realisateur realisateur);
}
