package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

//import lombok.NonNull;

import com.cordierlaurent.safetynet.model.MedicalRecord;

import lombok.NonNull;

@Repository
public class MedicalRecordRepository {
    
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    // pour récupérer la liste de tous les dossiers médicaux.
    public List<MedicalRecord> getMedicalRecords() {
        return new ArrayList<>(medicalRecords);  
    }

    // pour remplacer la liste de tous les dossiers médicaux.
    public void setMedicalRecords(List<MedicalRecord> newMedicalRecords) {
        medicalRecords = new ArrayList<>(newMedicalRecords);  
    }
    
    // ajouter un dossier médical.
    public void addMedicalRecord(@NonNull MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }

    // mise à jour du dossier médical avec la clef nom, prénom qui est l'identificateur unique.
    public boolean updateMedicalRecordByFirstNameAndLastName (String firstName, String lastName, @NonNull MedicalRecord medicalRecordToUpdate) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(firstName) &&
                medicalRecord.getLastName().equalsIgnoreCase(lastName)) {
                medicalRecords.set(medicalRecords.indexOf(medicalRecord), medicalRecordToUpdate);
                return true;
            }
        }
        return false;
    }

    // Suppression idem par clef unique avec lambda.
    public boolean deleteMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.removeIf(person ->
            person.getFirstName().equalsIgnoreCase(firstName) &&
            person.getLastName().equalsIgnoreCase(lastName)
        );
    }
    
}
