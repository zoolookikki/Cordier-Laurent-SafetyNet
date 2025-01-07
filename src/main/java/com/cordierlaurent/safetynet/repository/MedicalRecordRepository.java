package com.cordierlaurent.safetynet.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.MedicalRecord;

@Repository
public class MedicalRecordRepository extends CrudRepository<MedicalRecord> {
    
    @Override
    public boolean containsId(String[] id, MedicalRecord medicalRecord) {
        return medicalRecord.getFirstName().equalsIgnoreCase(id[0]) &&
                medicalRecord.getLastName().equalsIgnoreCase(id[1]);
    }

    public Optional<MedicalRecord> findMedicalRecordByUniqueKey(String[] id) {
        for (MedicalRecord medicalRecord : this.getModels()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(id[0]) && 
                    medicalRecord.getLastName().equalsIgnoreCase(id[1])) {
                return Optional.of(medicalRecord);
            }
        }
        return Optional.empty();
    }

    public String findBirthdateByUniqueKey(String[] id) {
        Optional<MedicalRecord> medicalRecord = findMedicalRecordByUniqueKey(id);
/*
        if (medicalRecord != null) {
            return medicalRecord.getBirthdate();
        }
        return new String();
*/
        return medicalRecord.map(MedicalRecord::getBirthdate).orElse("");
}

}
