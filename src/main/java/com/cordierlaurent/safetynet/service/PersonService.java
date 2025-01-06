package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.Util.DateUtil;
import com.cordierlaurent.safetynet.dto.PersonHealthExtentedInformationsDTO;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PersonService extends CrudService<Person> {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    protected boolean isSameModel(Person model, Person modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    @Override
    protected CrudRepository<Person> getRepository() {
        return personRepository;
    }

    public List<PersonHealthExtentedInformationsDTO> findPersonInfoByLastName(String lastName) {
        // créer une liste de DTO contenant prénom+nom+adresse+age+email+antécédents médicaux.
        List<PersonHealthExtentedInformationsDTO> personHealthExtentedInformationsDTOList = new ArrayList<>();  
        // recherche personnes par nom => liste de personnes.
        List<Person> persons = personRepository.findByLastName(lastName);
        
        // Pour chaque personne 
        for (Person person : persons) {
            log.debug("for each person=>firstName="+person.getFirstName()+",lastName="+person.getLastName());
            // Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
            MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordByUniqueKey(
                    new String[]{
                            person.getFirstName(), 
                            person.getLastName()});
            if (medicalRecord != null) {
                log.debug("  findPersonInfoByLastName/medicalRecordRepository.findMedicalRecordByUniqueKey==> NOT NULL");
                // Calculer l'age
                int age = DateUtil.CalculateAge(medicalRecord.getBirthdate());
                log.debug("  findFireByaddress/DateUtil.CalculateAge=>medicalRecord.getBirthdate()="+medicalRecord.getBirthdate()+",age="+age);
                if (age >= 0) {
                    // ajouter à la DTO.
                    personHealthExtentedInformationsDTOList.add(new PersonHealthExtentedInformationsDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            age,
                            person.getEmail(),
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                            ));
                }
            }
            else {
                log.debug("  findPersonInfoByLastName/medicalRecordRepository.findMedicalRecordByUniqueKey==> NULL");
            }
            
        }
        return personHealthExtentedInformationsDTOList;
    }    

}



