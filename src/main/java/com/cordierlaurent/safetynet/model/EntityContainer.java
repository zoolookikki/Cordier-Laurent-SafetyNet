package com.cordierlaurent.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  // Jackson a besoin d'un constructeur par défaut (sans argument) pour instancier l'objet lors de la désérialisation.
public class EntityContainer {
    private List<Person> persons;
    // pour que Jackson fasse automatiquement la désérialisation sur firestations au lieu fireStations.
    @JsonProperty("firestations")
    private List<FireStation> fireStations; 
    // idem medicalrecords au lieu de medicalRecords.
    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalRecords; 
}
