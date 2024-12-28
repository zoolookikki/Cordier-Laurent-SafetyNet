package com.cordierlaurent.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@Service
public class PersonService extends CrudService<Person> {

    @Autowired
    private PersonRepository personRepository;
    

    @Override
    protected boolean isSameModel(Person model, Person modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    @Override
    protected CrudRepository<Person> getRepository() {
        return personRepository;
    }    

}



