package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.Util.DateUtil;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service responsible for managing person entities and their interactions with other components.
 * Provides CRUD operations.
 */
@Service
@Log4j2
public class PersonService extends CrudService<Person> {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    /**
     * Compares two person models to determine if they are the same.
     * A person model is considered the same if it has the same first and last names (case insensitive).
     *
     * @param model the existing person model.
     * @param modelToVerify the person model to verify.
     * @return true if both models have the same first and last names, false otherwise.
     */
    @Override
    protected boolean isSameModel(Person model, Person modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    /**
     * Provides the repository used for CRUD operations on persons.
     *
     * @return the repository instance managing persons.
     */
    @Override
    protected CrudRepository<Person> getRepository() {
        return personRepository;
    }

    /**
     * Retrieves detailed information about persons with a given last name.
     *
     * @param lastName the last name to search for.
     * @return a list of PersonInformationsDTO representing the detailed information of matching persons.
     * @throws IllegalArgumentException if lastName is null or blank.
     */
    /*
    créer une liste de DTO contenant prénom+nom+adresse+age+email+antécédents médicaux.
    recherche personnes par nom => liste de personnes.
    Pour chaque personne 
        Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
        Calculer l'age 
        Ajouter à la liste de DTO
    */         
    public List<PersonInformationsDTO> findPersonInfoByLastName(String lastName) {
        log.debug("START findPersonInfoByLastName");
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName cannot be null or blank");
        }
        log.debug("lastName="+lastName);

        // créer une liste de DTO contenant prénom+nom+adresse+age+email+antécédents médicaux.
        List<PersonInformationsDTO> personInformationsDTOList = new ArrayList<>();  
        // recherche personnes par nom => liste de personnes.
        List<Person> persons = personRepository.findByLastName(lastName);

        // Pour chaque personne 
        for (Person person : persons) {
            log.debug("for each person=>firstName="+person.getFirstName()+",lastName="+person.getLastName());
            // Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findMedicalRecordByUniqueKey(
                    new String[]{
                            person.getFirstName(), 
                            person.getLastName()});
            if (medicalRecordOptional.isPresent()) {
                MedicalRecord medicalRecord = medicalRecordOptional.get();
                log.debug("  medicalRecordRepository.findMedicalRecordByUniqueKey==> true");
                // Calculer l'age
                int age = DateUtil.calculateAge(medicalRecord.getBirthdate());
                log.debug("  DateUtil.CalculateAge=>medicalRecord.getBirthdate()="+medicalRecord.getBirthdate()+",age="+age);
                if (age >= 0) {
                    // ajouter à la DTO.
                    personInformationsDTOList.add(new PersonInformationsDTO(
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
                log.debug("  medicalRecordRepository.findMedicalRecordByUniqueKey==> false");
            }
            
        }
        log.debug("END findPersonInfoByLastName");
        return personInformationsDTOList;
    }

    /**
     * Retrieves a set of email addresses for all persons living in a specified city.
     *
     * @param city the name of the city to search for.
     * @return a Set of email addresses, sorted in natural order.
     * @throws IllegalArgumentException if city is null or blank.
     */
    /*
    recherche personnes par ville => liste de personnes.
    pour chaque personne 
        ajout de l'adresse e-mail dans la liste (attention au filtrage des doublons).
    */
    public Set<String> findEmailsByCity(String city) {
        log.debug("START findEmailsByCity");
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or blank");
        }        
        log.debug("city="+city);
        
        // warning : for duplicate emails => Set type list.
        // interesting to sort the list also => ​​TreeSet.
        Set<String> emails = new TreeSet<>();

        // recherche personnes par ville => liste de personnes.
        List<Person> persons = personRepository.findByCity(city);

        for (Person person : persons) {
            log.debug("for each person=>firstName="+person.getFirstName()+",lastName="+person.getLastName()+",email="+person.getEmail());
            if (!person.getEmail().isBlank()) {
                // ajout de l'adresse e-mail dans la liste
                if (!emails.add(person.getEmail())) {
                    log.debug("  duplicate : not added");
                }
            }
        }
        log.debug("END findEmailsByCity");
       return emails;
    }    

}



