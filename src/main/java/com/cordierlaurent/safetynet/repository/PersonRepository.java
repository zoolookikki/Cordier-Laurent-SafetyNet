package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.Person;

@Repository
public class PersonRepository extends CrudRepository<Person> {
    
    @Override
    public boolean containsId(String[] id, Person person) {
        // id invalide => clef unique = prénom+nom.
        if (id.length != 2) {
            return false; 
        }
        return person.getFirstName().equalsIgnoreCase(id[0]) &&
                person.getLastName().equalsIgnoreCase(id[1]);
    }
    
    public List<Person> findByAddresses(List<String> addresses){
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (addresses.contains(person.getAddress())) {
                persons.add(person);
            }
        }
        return persons;
    }
    
    public List<Person> findByAddress(String address) {
        return findByAddresses(List.of(address));
    }
    
    public List<Person> findByLastName(String lastName){
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                persons.add(person);
            }
        }
        return persons;
    }

    public List<Person> findByCity(String city) {
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (person.getCity().equalsIgnoreCase(city)) {
                persons.add(person);
            }
        }
        return persons;
    }
    
}

