package com.cordierlaurent.safetynet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A container class for aggregating entity lists such as Person}, FireStation, and MedicalRecord.
 * 
 * <p>This class is used to encapsulate multiple lists of entities that are deserialized from JSON files.</p> 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor  // Jackson needs a default constructor (no arguments) to instantiate the object during deserialization.
public class EntityContainer {
    private List<Person> persons;
    // To make Jackson automatically deserialize to firestations instead of fireStations.
    @JsonProperty("firestations")
    private List<FireStation> fireStations; 
    // To make Jackson automatically deserialize to medicalrecords instead of medicalRecords.
    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalRecords; 
}
