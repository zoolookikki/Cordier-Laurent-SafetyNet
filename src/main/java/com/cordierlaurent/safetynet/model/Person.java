package com.cordierlaurent.safetynet.model;

import lombok.Data;

import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    
    @NotBlank(message = "Le prénom est obligatoire")    
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")    
    private String lastName;

    @NotBlank(message = "L'adresse est obligatoire")    
    private String address;

    @NotBlank(message = "La ville est obligatoire")    
    private String city;

    @NotBlank(message = "Le code postal est obligatoire")    
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit être sur 5 chiffres")
    private String zip; 

    @NotBlank(message = "Le numéro de téléphone est obligatoire")    
    @Pattern(regexp = "\\d{10}", message = "Le numéro de téléphone doit être composé de 10 chiffres")
    private String phone;

    @NotBlank(message = "L'email est obligatoire")    
    @Email(message = "L'email doit être valide")
    private String email;
    
}
