package com.cordierlaurent.safetynet.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class EntityContainer {
    private List<Person> persons;
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;
}
