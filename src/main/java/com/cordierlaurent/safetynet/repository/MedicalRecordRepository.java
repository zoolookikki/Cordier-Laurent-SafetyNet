package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

//import lombok.NonNull;

import com.cordierlaurent.safetynet.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {
    
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getMedicalRecords() {
        return new ArrayList<>(medicalRecords);  
    }

    public void setMedicalRecords(List<MedicalRecord> newMedicalRecords) {
        medicalRecords = new ArrayList<>(newMedicalRecords);  
    }
    
}
