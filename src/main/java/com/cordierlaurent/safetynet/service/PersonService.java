package com.cordierlaurent.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    
    private void validation(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getFirstName().isEmpty() || person.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Firstname or lastname is empty");
        }
    }
    
    public boolean createPerson(Person personToCreate) {

        // validation des champs dans le service.
        validation(personToCreate);
        
        // contrôle d'unicité dans le service.
        for (Person person : personRepository.getPersons()) {
            if (person.getFirstName().equalsIgnoreCase(personToCreate.getFirstName()) &&
                    person.getLastName().equalsIgnoreCase(personToCreate.getLastName())) {
                return false;
            }
        }
        personRepository.addPerson(personToCreate);
        return true;
    }
    
}
