package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.PersonService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
// la route est définie ici dans la classe fille.
@RequestMapping("/person")
public class PersonController extends CrudController<Person>{

    @Autowired
    private PersonService personService;
    
    @Override
    protected CrudService<Person> getService() {
        return personService;
    }

}
