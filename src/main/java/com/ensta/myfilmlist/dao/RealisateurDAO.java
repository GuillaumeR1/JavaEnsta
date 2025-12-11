package com.ensta.myfilmlist.dao;

import com.ensta.myfilmlist.model.Realisateur;
import java.util.List;
import java.util.Optional;

public interface RealisateurDAO {
    
    // Déjà défini dans l'énoncé
    List<Realisateur> findAll(); 
    Optional<Realisateur> findById(long id);
    Realisateur findByNomAndPrenom(String nom, String prenom); 
    
    // Pour le bonus : création et modification
    //Realisateur save(Realisateur realisateur);    
    //Realisateur update(Realisateur realisateur); 
}