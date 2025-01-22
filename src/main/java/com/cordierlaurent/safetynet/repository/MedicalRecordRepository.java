package com.cordierlaurent.safetynet.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.MedicalRecord;

/**
 * Repository class for managing MedicalRecord entities.
 * 
 * <p>Extends CrudRepository to provide CRUD operations and specific methods  for handling medical records.</p> 
 * 
 */
@Repository
public class MedicalRecordRepository extends CrudRepository<MedicalRecord> {

    /**
     * Validates the structure of a unique identifier.
     * 
     * <p>A valid identifier consists of exactly two parts:</p>
     * <ul>
     *   <li>The first name of the person.</li>
     *   <li>The last name of the person.</li>
     * </ul>
     * 
     * @param id the unique identifier as an array of strings: [firstName, lastName].
     * @throws IllegalArgumentException if id does not have exactly two elements.
     * @throws NullPointerException if id is null.
     */
    private void validId (String[] id) {
        Objects.requireNonNull(id, "id cannot be null");
        // invalid id => unique key = first name+last name.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be firtname + lastname");
        }
    }
    
    /**
     * Checks if a given MedicalRecord matches a unique identifier.
     * 
     * @param id the unique identifier as an array of strings: [firstName, lastName].
     * @param medicalRecord the medical record entity to compare with.
     * @return true if the medical record matches the given identifier, false otherwise.
     * @throws IllegalArgumentException if id does not have exactly two elements.
     * @throws NullPointerException if id or medicalRecord is null.
     */
    @Override
    public boolean containsId(String[] id, MedicalRecord medicalRecord) {
        Objects.requireNonNull(medicalRecord, "medicalRecord cannot be null");
        validId(id);
        return medicalRecord.getFirstName().equalsIgnoreCase(id[0]) &&
                medicalRecord.getLastName().equalsIgnoreCase(id[1]);
    }

    /**
     * Finds a medical record by its unique key (first name and last name).
     * 
     * @param id the unique identifier as an array of strings: [firstName, lastName].
     * @return an Optional containing the matching MedicalRecord, or empty if not found.
     * @throws IllegalArgumentException if id does not have exactly two elements.
     * @throws NullPointerException if id is null.
     */
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


    /**
     * Finds the birthdate of a person based on their unique key (first name and last name).
     * 
     * @param id the unique identifier as an array of strings: [firstName, lastName].
     * @return the birthdate as a String, or an empty string if no record is found.
     * @throws IllegalArgumentException if id does not have exactly two elements.
     * @throws NullPointerException if id is null.
     */
    public String findBirthdateByUniqueKey(String[] id) {
        validId(id);
        Optional<MedicalRecord> medicalRecord = findMedicalRecordByUniqueKey(id);
/*
        equivalent to :
        if (medicalRecord != null) {
            return medicalRecord.getBirthdate();
        }
        return new String();
*/
        return medicalRecord.map(MedicalRecord::getBirthdate).orElse("");
    }

}
