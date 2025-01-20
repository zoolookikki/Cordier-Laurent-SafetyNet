package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.Person;

import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
public class PersonRepository extends CrudRepository<Person> {
    
    @Override
    public boolean containsId(String[] id, Person person) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(person, "person cannot be null");
        // id invalide => clef unique = prénom+nom.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be firtname + lastname");
        }
        return  person.getFirstName().equalsIgnoreCase(id[0]) &&
                person.getLastName().equalsIgnoreCase(id[1]);
    }
    
    public List<Person> findByAddresses(Set<String> addresses){
        Objects.requireNonNull(addresses, "addresses cannot be null");
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            // pas trouvé d'équivalent genre contains ignore case...
/*
             if (addresses.contains(person.getAddress())) {
                persons.add(person);
*/
            for (String address : addresses) {
                if (person.getAddress().equalsIgnoreCase(address)) {
                    persons.add(person);
                    // une fois trouvé, on arrête.
                    break; 
                }
            }
        }
        return persons;
    }
    
    public List<Person> findByAddress(String address) {
        Objects.requireNonNull(address, "address cannot be null");
        if (address.isBlank()) { 
            throw new IllegalArgumentException("Address cannot be empty, or blank");
        }        
        return findByAddresses(Set.of(address));
    }
    
    public List<Person> findByLastName(String lastName){
        Objects.requireNonNull(lastName, "lastName cannot be null");
        if (lastName.isBlank()) { 
            throw new IllegalArgumentException("Lastname cannot be empty, or blank");
        }        
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                persons.add(person);
            }
        }
        return persons;
    }

    public List<Person> findByCity(String city) {
        Objects.requireNonNull(city, "city cannot be null");
        if (city.isBlank()) { 
            throw new IllegalArgumentException("City cannot be empty, or blank");
        }        
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (person.getCity().equalsIgnoreCase(city)) {
                persons.add(person);
            }
        }
        return persons;
    }
    
}

