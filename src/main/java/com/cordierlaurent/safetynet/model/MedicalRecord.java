package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
@NoArgsConstructor // Jackson a besoin d'un constructeur par défaut (sans argument) pour instancier l'objet lors de la désérialisation.
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
