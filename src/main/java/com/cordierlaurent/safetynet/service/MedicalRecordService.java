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

@Service
@Log4j2
public class MedicalRecordService extends CrudService<MedicalRecord> {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    

    @Override
    protected boolean isSameModel(MedicalRecord model, MedicalRecord modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    @Override
    protected CrudRepository<MedicalRecord> getRepository() {
        return medicalRecordRepository;
    }
    
    public int age(Person person) {
        Objects.requireNonNull(person, "person cannot be null");

        // on retrouve sa date de naissance via la clef unique dans MedicalRecord.
        String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(
                new String[]{
                        person.getFirstName(), 
                        person.getLastName()});
        return DateUtil.calculateAge(birthdate);        
    }
 
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
