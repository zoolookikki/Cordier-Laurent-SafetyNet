package com.cordierlaurent.safetynet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.service.AlertService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class AlertController {

    @Autowired
    AlertService alertService;
    
    // implémentation de l'url qui retourne la liste des enfants <= 18 ans à une adresse donnée avec les membres du foyer : http://localhost:8080/childAlert?address=<address>
    /*
    Rechercher la liste des personnes habitant à cette adresse.
    Pour chaque personne :
        Trouver l'age via la clef unique dans MedicalRecord
        Si moins de 18 ans 
            Ajouter dans la nouvelle DTO (prénom,nom,age).
            Complêter la DTO avec la liste des membres du foyer (ayant donc la même adresse). Attention à exclure la personne en cours de traitement.
    Renvoyer un json sous la forme :
    {
        "children": [
            {
                "firstName": "wwww",
                "lastName": "xxxx",
                "age": ?,
                "householdMembers": [
                    {
                        "firstName": "aaaa",
                        "lastName": "bbbb",
                        "address": "cccc",
                        "phone": "dddd"
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff",
                        "address": "gggg",
                        "phone": "hhhh"
                    }
                ]
            },
            {
                "firstName": "yyyy",
                "lastName": "zzzz",
                "age": ?,
                "householdMembers": [
                    {
                        "firstName": "aaaa",
                        "lastName": "bbbb",
                        "address": "cccc",
                        "phone": "dddd"
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff",
                        "address": "gggg",
                        "phone": "hhhh"
                    }
                ]
            }
        ]
    }
    */
    
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture + route childAlert.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildAlert(@RequestParam("address") String address){
        List<ChildAlertDTO> childAlertDTO = alertService.findChilddByAddress(address);

        // la liste de personnes est vide => rien trouvé.
        if (childAlertDTO.isEmpty()) {
            log.info("getChildAlertDTO : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .build(); // là, il ne faut peut être rien dire dans ce cas de figure => build : réponse sans body.
        }
        // il y a au moins une personne => ok.
        log.info("getChildAlertDTO : " + this.getClass().getSimpleName() + " : " +  childAlertDTO.size() + " trouvé(s) ");
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(childAlertDTO); 
    }

}
