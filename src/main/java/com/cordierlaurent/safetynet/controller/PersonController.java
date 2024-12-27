package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.PersonService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class PersonController {

    @Autowired
    private PersonService personService;
    
    @Autowired
    private JsonDataRepository jsonDataRepository;
    
    // @PostMapping : mappe une requête HTTP POST à une méthode de contrôleur : ici en POST. 
    // ResponseEntity représente l'ensemble de la réponse HTTP envoyée au client (corps, status, entête http) : <Person> => objet retourné en Json.
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet Person (désérialisaion automatique : json -> person).
    // ? au lieu de Person sinon erreur type mismatch car ici on renvoit soit Person soit un String.
    @PostMapping("/person")
    public ResponseEntity<?> addPerson(@RequestBody Person person) {

        // le contrôleur doit vérifier le contenu de la requête avant de le transmettre au service.
        // isBlank() plus strict que isEmpty() => caractères blancs (espaces, tabulations, sauts de ligne).
        if (person.getFirstName() == null || person.getFirstName().isBlank() || 
                person.getLastName() == null || person.getLastName().isBlank()) {
            log.info("vérification contrôleur : entrée utilisateur vide");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("first and last name are required");
        }
        
        // vérification de l'unicité par le service => logique métier.
        if (!personService.isUnique(person)) {
            log.info("vérification contrôleur : la personne n'est pas unique");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("Person already exists.");
        }
        
        // c'est ok.
        log.info("vérification contrôleur : tout est ok");
        personService.addModel(person);
        jsonDataRepository.save(); // je mets à jour le fichier json ici.
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 created => préférable à 200 ok.
                .body(person); // on retourne person même si c'est identique ici pour respecter le standard.
    }
}
