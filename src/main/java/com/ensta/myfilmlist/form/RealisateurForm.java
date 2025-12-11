package com.ensta.myfilmlist.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class RealisateurForm {
    
    // Un nom est non null et non vide
    @NotBlank(message = "Le nom du réalisateur ne doit pas être vide.")
    private String nom;
    
    // Un prénom est non null et non vide
    @NotBlank(message = "Le prénom du réalisateur ne doit pas être vide.")
    private String prenom;
    
    // Une date de naissance est obligatoire
    @NotNull(message = "La date de naissance est obligatoire.")
    private LocalDate dateNaissance;

    // ... Getters & Setters ...
}