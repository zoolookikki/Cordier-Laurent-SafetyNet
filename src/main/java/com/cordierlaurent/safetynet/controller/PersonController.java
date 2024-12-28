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
public class PersonController extends CrudController<String[], Person>{

    @Autowired
    private PersonService personService;
    
    @Override
    protected boolean checkModel(Person model) {
        return (model.getFirstName() != null && !model.getFirstName().isBlank() && 
                model.getLastName() != null && !model.getLastName().isBlank());
    }

    @Override
    protected CrudService<String[], Person> getService() {
        return personService;
    }
}
