package com.cordierlaurent.safetynet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.dto.PersonHealthExtentedInformationsDTO;
import com.cordierlaurent.safetynet.service.PersonService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class PersonInfosController {
    
    @Autowired
    private PersonService personService;

    // implémentation de l'url qui retourne les informations détaillées des personnes portant un nom de famille donné : http://localhost:8080/personInfolastName=<lastName> 
    /*
    créer une liste de DTO contenant prénom+nom+adresse+age+email+antécédents médicaux.
    recherche personnes par nom => liste de personnes.
    Pour chaque personne 
        Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
        Calculer l'age 
        Ajouter à la liste de DTO 
    Renvoyer un json sous la forme :
    [
        {
            "firstName": "xxxx",
            "lastName": "xxxx",
            "address": "xxxx",
            "age": ??,
            "email": "xxxx",
            "medications": ["xxxx", "xxxx"],
            "allergies": ["xxxx", "xxxx"],
        },
        {
            "firstName": "yyyy",
            "lastName": "yyyy",
            "address": "yyyy",
            "age": ??,
            "email": "yyyy",
            "medications": ["yyyy", "yyyy"],
            "allergies": ["yyyy", "yyyy"],
        }
    ]
    */
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture => ici il est un peu spécial car directement à la racine.
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode
    @GetMapping("/personInfolastName={lastName}")
    public ResponseEntity<?> getPersonInfoLastName(@PathVariable("lastName") String lastName){
        log.debug("appel de : /personInfolastName=<lastName>");
        
        List<PersonHealthExtentedInformationsDTO> personHealthExtentedInformationsDTOList = personService.findPersonInfoByLastName(lastName);

        // la liste de personnes est vide => rien trouvé.
        if (personHealthExtentedInformationsDTOList.isEmpty()) {
            log.info("getPersonInfoLastName : " + this.getClass().getSimpleName() + " : non trouvé");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .build(); // là, il ne faut peut être rien dire dans ce cas de figure => build : réponse sans body.
        }
        // il y a au moins une personne => ok.
        log.info("getPersonInfoLastName : " + this.getClass().getSimpleName() + " : " +  personHealthExtentedInformationsDTOList.size() + " trouvé(s)");
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(personHealthExtentedInformationsDTOList); 
    }

}
