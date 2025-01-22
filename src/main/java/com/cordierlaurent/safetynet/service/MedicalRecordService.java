package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.Util.DateUtil;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service responsible for managing medical records.
 * Provides CRUD operations.
 */
@Service
@Log4j2
public class MedicalRecordService extends CrudService<MedicalRecord> {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    

    /**
     * Compares two medical record models to determine if they are the same.
     * A medical record is considered the same if it has the same first name and last name.
     *
     * @param model the existing medical record model.
     * @param modelToVerify the medical record model to verify.
     * @return true if both models have the same first and last names, false otherwise.
     */
    @Override
    protected boolean isSameModel(MedicalRecord model, MedicalRecord modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    /**
     * Provides the repository used for CRUD operations on medical records.
     *
     * @return the repository instance managing medical records.
     */
    @Override
    protected CrudRepository<MedicalRecord> getRepository() {
        return medicalRecordRepository;
    }
    
    /**
     * Calculates the age of a person based on their medical record's birthdate.
     *
     * @param person the person whose age is to be calculated.
     * @return the calculated age, or -1 if the birthdate is invalid or not found.
     * @throws NullPointerException if the person is null.
     */
    public int age(Person person) {
        Objects.requireNonNull(person, "person cannot be null");

        // we find his date of birth via the unique key in MedicalRecord.
        String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(
                new String[]{
                        person.getFirstName(), 
                        person.getLastName()});
        return DateUtil.calculateAge(birthdate);        
    }
 
    /**
     * Creates a list of PersonInformationsDTO objects for the given list of persons.
     *
     * @param persons the list of persons to process.
     * @return a list of PersonInformationsDTO, with one entry per person.
     * @throws NullPointerException if the list of persons is null}.
     */
    public List<PersonInformationsDTO> getPersonInformationsDTOs(List<Person> persons){
        Objects.requireNonNull(persons, "list persons cannot be null");

        List<PersonInformationsDTO> personInformationsDTOs = new ArrayList<>();

        for (Person person : persons) {
            log.debug("    for each person=>firstName="+person.getFirstName()+",lastName="+person.getLastName());
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findMedicalRecordByUniqueKey(
                    new String[]{
                            person.getFirstName(), 
                            person.getLastName()});
            if (medicalRecordOptional.isPresent()) {
                MedicalRecord medicalRecord = medicalRecordOptional.get();
                log.debug("      medicalRecordRepository.findMedicalRecordByUniqueKey=> true");
                int age = DateUtil.calculateAge(medicalRecord.getBirthdate());
                log.debug("      DateUtil.CalculateAge=>medicalRecord.getBirthdate()="+medicalRecord.getBirthdate()+",age="+age);
                if (age >= 0) {
                    personInformationsDTOs.add(new PersonInformationsDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            person.getPhone(),
                            age,
                            person.getEmail(), 
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                            ));
                }
            }
            else {
                log.debug("      medicalRecordRepository.findMedicalRecordByUniqueKey=> false");
            }
        }
        
        return personInformationsDTOs; 
    }
    
    
}
