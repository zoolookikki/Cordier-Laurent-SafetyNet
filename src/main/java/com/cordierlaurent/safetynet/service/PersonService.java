package com.cordierlaurent.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    
    // contrôle d'unicité dans le service (métier).
    public boolean isUnique(@NonNull Person personToVerify) {
        for (Person person : personRepository.getModels()) {
            if (person.getFirstName().equalsIgnoreCase(personToVerify.getFirstName()) &&
                    person.getLastName().equalsIgnoreCase(personToVerify.getLastName())) {
                log.debug("PersonService : unicité NOK");
                return false;
            }
        }
        log.debug("PersonService : unicité OK");
        return true;
    }
    
    public void addPerson(@NonNull Person personToAdd) {
        personRepository.addModel(personToAdd);
        log.debug("PersonService : ajout personToAdd OK");
    }
    
}
