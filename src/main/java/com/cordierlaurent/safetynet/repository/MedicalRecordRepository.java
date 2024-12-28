package com.cordierlaurent.safetynet.repository;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.MedicalRecord;

@Repository
public class MedicalRecordRepository extends CrudRepository<MedicalRecord> {
    
    @Override
    public boolean containsId(String[] id, MedicalRecord medicalRecord) {
        return medicalRecord.getFirstName().equalsIgnoreCase(id[0]) &&
                medicalRecord.getLastName().equalsIgnoreCase(id[1]);
    }

    
}
