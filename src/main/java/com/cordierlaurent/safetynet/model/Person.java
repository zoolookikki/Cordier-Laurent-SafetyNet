package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
Getter et Setter pour le repository
toString pour les traces.
equals si je dois comparer les entités + utilisation comparaison containsExactly assertJ objet identique et non pas la référence mémoire.
 */
@Data // je préfère les utiliser 1 par 1 pour le moment.
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
@NoArgsConstructor // Jackson a besoin d'un constructeur par défaut (sans argument) pour instancier l'objet lors de la désérialisation.
// @SuppressWarnings("unused") // pour Eclipse => inutile après installation de lombok dans Eclipse.
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip; 
    private String phone;
    private String email;
}
