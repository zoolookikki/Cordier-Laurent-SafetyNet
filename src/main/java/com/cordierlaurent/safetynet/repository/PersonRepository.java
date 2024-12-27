package com.cordierlaurent.safetynet.repository;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.Person;

@Repository
public class PersonRepository extends CrudRepository<String[], Person> {
    
    @Override
    protected boolean isUnique(String[] id, Person person) {
        return person.getFirstName().equalsIgnoreCase(id[0]) &&
                person.getLastName().equalsIgnoreCase(id[1]);
    }
    
}
