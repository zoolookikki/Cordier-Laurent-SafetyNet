package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.Person;

@Repository
public class PersonRepository extends CrudRepository<Person> {
    
    @Override
    public boolean containsId(String[] id, Person person) {
        return person.getFirstName().equalsIgnoreCase(id[0]) &&
                person.getLastName().equalsIgnoreCase(id[1]);
    }
    
    public List<Person> findByAddresses(List<String> addresses){
//        return new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        for (Person person : this.getModels()) {
            if (addresses.contains(person.getAddress())) {
                persons.add(person);
            }
        }
        return persons;
    }
    
}

