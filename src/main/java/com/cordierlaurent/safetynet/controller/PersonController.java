package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.PersonService;

import lombok.extern.log4j.Log4j2;

/**
 * Controller responsible for handling HTTP requests related to Person entities.
 * 
 * This controller extends CrudController to reuse generic CRUD functionalities.
 * 
 */
@RestController
@Log4j2
//The base route for this controller.
@RequestMapping("/person")
public class PersonController extends CrudController<Person>{

    @Autowired
    private PersonService personService;
    
    /**
     * Provides the specific service for abstract CrudController.
     *
     * @return The instance of the service.
     */
    @Override
    protected CrudService<Person> getService() {
        return personService;
    }

}
