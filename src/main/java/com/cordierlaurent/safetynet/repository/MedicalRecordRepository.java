package com.cordierlaurent.safetynet.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.MedicalRecord;

@Repository
public class MedicalRecordRepository extends CrudRepository<MedicalRecord> {

    private void validId (String[] id) {
        Objects.requireNonNull(id, "id cannot be null");
        // id invalide => clef unique = prénom+nom.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be firtname + lastname");
        }
    }
    
    @Override
    public boolean containsId(String[] id, MedicalRecord medicalRecord) {
        Objects.requireNonNull(medicalRecord, "medicalRecord cannot be null");
        validId(id);
        return medicalRecord.getFirstName().equalsIgnoreCase(id[0]) &&
                medicalRecord.getLastName().equalsIgnoreCase(id[1]);
    }

    public Optional<MedicalRecord> findMedicalRecordByUniqueKey(String[] id) {
        validId(id);
        for (MedicalRecord medicalRecord : this.getModels()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(id[0]) && 
                    medicalRecord.getLastName().equalsIgnoreCase(id[1])) {
                return Optional.of(medicalRecord);
            }
        }
        return Optional.empty();
    }

    public String findBirthdateByUniqueKey(String[] id) {
        validId(id);
        Optional<MedicalRecord> medicalRecord = findMedicalRecordByUniqueKey(id);
/*
        équivalent à :
        if (medicalRecord != null) {
            return medicalRecord.getBirthdate();
        }
        return new String();
*/
        return medicalRecord.map(MedicalRecord::getBirthdate).orElse("");
    }

}
