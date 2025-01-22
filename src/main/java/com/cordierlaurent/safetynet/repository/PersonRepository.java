package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.Person;

import lombok.extern.log4j.Log4j2;

/**
 * Repository class for managing  Person entities.
 * 
 * <p>Extends CrudRepository to provide CRUD operations and additional methods specific to Person data management.</p>
 * 
 */
@Repository
@Log4j2
public class PersonRepository extends CrudRepository<Person> {
    
    /**
     * Checks if a given Person matches a unique identifier.
     * 
     * @param id the unique identifier as an array of strings: [firstName, lastName].
     * @param person the person entity to compare with.
     * @return true if the person matches the given identifier, false otherwise.
     * @throws IllegalArgumentException if id does not have exactly two elements.
     * @throws NullPointerException if id or person is null.
     */
    @Override
    public boolean containsId(String[] id, Person person) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(person, "person cannot be null");
     // invalid id => unique key = first name+last name.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be firtname + lastname");
        }
        return  person.getFirstName().equalsIgnoreCase(id[0]) &&
                person.getLastName().equalsIgnoreCase(id[1]);
    }
    
    /**
     * Finds all persons living at any of the specified addresses.
     * 
     * @param addresses a set of addresses to search for.
     * @return a list of persons living at the specified addresses.
     * @throws NullPointerException if addresses is null.
     */
    public List<Person> findByAddresses(Set<String> addresses){
        Objects.requireNonNull(addresses, "addresses cannot be null");
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
          // not found an equivalent like contains ignore case...
/*
             if (addresses.contains(person.getAddress())) {
                persons.add(person);
*/
            for (String address : addresses) {
                if (person.getAddress().equalsIgnoreCase(address)) {
                    persons.add(person);
                    break; // once found, we stop. 
                }
            }
        }
        return persons;
    }
    
    /**
     * Finds all persons living at a specific address.
     * 
     * @param address the address to search for.
     * @return a list of persons living at the specified address.
     * @throws IllegalArgumentException if address is empty or blank.
     * @throws NullPointerException if address is null.
     */
    public List<Person> findByAddress(String address) {
        Objects.requireNonNull(address, "address cannot be null");
        if (address.isBlank()) { 
            throw new IllegalArgumentException("Address cannot be empty, or blank");
        }        
        return findByAddresses(Set.of(address));
    }
    
    /**
     * Finds all persons with the specified last name.
     * 
     * @param lastName the last name to search for.
     * @return a list of persons with the specified last name.
     * @throws IllegalArgumentException if lastName is empty or blank.
     * @throws NullPointerException if lastName is null.
     */
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

    /**
     * Finds all persons living in a specific city.
     * 
     * @param city the city to search for.
     * @return a list of persons living in the specified city.
     * @throws IllegalArgumentException if city is empty or blank.
     * @throws NullPointerException if city is null.
     */
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

