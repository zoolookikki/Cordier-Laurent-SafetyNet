package com.cordierlaurent.safetynet.model;

import lombok.Data;

import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

/*
Getter et Setter pour le repository
toString pour les traces.
equals si je dois comparer les entités + utilisation comparaison containsExactly assertJ objet identique et non pas la référence mémoire.
 */
@Data 
// je préfère les utiliser 1 par 1 pour le moment.
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
// @SuppressWarnings("unused") // pour Eclipse => inutile après installation de lombok dans Eclipse.
public class Person {
    
    @NotNull(message = "Le prénom ne peut pas être null")
    @NotBlank(message = "Le prénom est obligatoire")    
    private String firstName;

    @NotNull(message = "Le nom ne peut pas être null")
    @NotBlank(message = "Le nom est obligatoire")    
    private String lastName;

    @NotNull(message = "L'adresse ne peut pas être null")
    @NotBlank(message = "L'adresse est obligatoire")    
    private String address;

    @NotNull(message = "La ville ne peut pas être null")
    @NotBlank(message = "La ville est obligatoire")    
    private String city;

    @NotNull(message = "Le code postal ne peut pas être null")
    @NotBlank(message = "Le code postal est obligatoire")    
    private String zip; 

    @NotNull(message = "Le numéro de téléphone ne peut pas être null")
    @NotBlank(message = "Le numéro de téléphone est obligatoire")    
    private String phone;

    @NotNull(message = "L'email ne peut pas être null")
    @NotBlank(message = "L'email est obligatoire")    
    @Email(message = "L'email doit être valide")
    private String email;
    
}
