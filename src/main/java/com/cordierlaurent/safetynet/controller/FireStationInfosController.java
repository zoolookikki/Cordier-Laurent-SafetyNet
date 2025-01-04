package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.dto.FireDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.service.FireStationService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class FireStationInfosController {

    @Autowired
    private FireStationService fireStationService;
    
    // implémentation de l'url qui demande la liste des personnes corresponant à une station : http://localhost:8080/firestation?stationNumber=<station_number>
    /*
    recherche firestations par station => liste d'adresses.
    recherche personnes par adresse => liste de personnes.
    dans cette liste de personnes il ne faut avoir que le prénom + nom + adresse + téléphone.
    calculer le nombre de personnes > et <=18. 
    Renvoyer un json sous la forme :
    {
        "persons": [
        {
          "firstName": "xxxx",
          "lastName": "xxxx",
          "address": "xxxx",
          "phone": "xxxx"
        },
        {
          "firstName": "yyyy",
          "lastName": "yyyy",
          "address": "yyyy",
          "phone": "yyyy"
        }
      ],
      "numberOfAdults": ?,
      "numberOfChildren": ?
    }
    */
    
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/firestation")
    public ResponseEntity<?> getPersonsCoveredByFireStation(@RequestParam("stationNumber") int stationNumber){
        log.debug("appel de : /firestation?stationNumber=<station_number>");
        
        PersonsCoveredByFireStationDTO personsCoveredByFireStationDTO = fireStationService.findPersonsCoveredByFireStation(stationNumber);

        // la liste de personnes est vide => rien trouvé.
        if (personsCoveredByFireStationDTO.getPersonBasicInformationsDTO().isEmpty()) {
            log.info("getPersonsCoveredByFireStation : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .build(); // là, il ne faut peut être rien dire dans ce cas de figure => build : réponse sans body.
        }
        // il y a au moins une personne => ok.
        log.info("getPersonsCoveredByFireStation : " + this.getClass().getSimpleName() + " : " +  personsCoveredByFireStationDTO.getPersonBasicInformationsDTO().size() + " trouvé(s) ");
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(personsCoveredByFireStationDTO); 
    }

    // implémentation de l'url qui retourne les personnes habitants à une adresse donnée avec le numéro de la caserne de pompiers la déservant. 
    // Cette liste doit inclure également le numéro de téléphone, l'âge et les antécédents mécidaux de chaque personne.
    // http://localhost:8080/fire?address=<address>
    /*
    Rechercher la station correspondante à cette adresse.
    Si elle existe, rechercher la liste des personnes habitant à cette adresse.
    créer une nouvelle DTO1 contenant prénom+nom+téléphone+age+antécédents médicaux.
    Pour chaque personne 
        Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
        Calculer l'age 
        Ajouter à la DTO1 
    Créer une nouvelle DTO2 dans laquelle on met la station + le contenu de la DTO1.
    Renvoyer un json sous la forme :
    {
      "station": ?,
      "persons": [
        {
          "firstName": "xxx",
          "lastName": "xxx",
          "phone": "???-???-????",
          "age": ??,
          "medications": ["xxxx", "xxxx"],
          "allergies": ["xxxx", "xxxx"]
        },
        {
          "firstName": "yyyy",
          "lastName": "yyyy",
          "phone": "???-???-????",
          "age": ??,
          "medications": ["yyyy", "yyyy"],
          "allergies": ["yyyy", "yyyy"]
        }
      ]
    }
    */
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/fire")
    public ResponseEntity<?> getFireByAddresse(@RequestParam("address") String address){
        log.debug("appel de : /fire?address=<address>");
        
        FireDTO fireDTO = fireStationService.findFireByaddress(address);

        // la liste de personnes est vide => rien trouvé.
        if (fireDTO.getStation() <=0 || fireDTO.getPersons().isEmpty()) {
            log.info("getFireByAddresse : " + this.getClass().getSimpleName() + " : non trouvé");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .build(); // là, il ne faut peut être rien dire dans ce cas de figure => build : réponse sans body.
        }
        // il y a au moins une personne => ok.
        log.info("getFireByAddresse : " + this.getClass().getSimpleName() + " : " +  fireDTO.getPersons().size() + " trouvé(s)");
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(fireDTO); 
    }

    
}
