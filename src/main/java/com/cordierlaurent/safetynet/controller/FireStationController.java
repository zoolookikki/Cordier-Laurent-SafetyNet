package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
// La route est définie ici dans la classe fille.
@RequestMapping("/firestation")
public class FireStationController extends CrudController<FireStation>{

    @Autowired
    private FireStationService fireStationService;
    
    @Override
    protected boolean checkModel(FireStation model) {
        return (model.getAddress() != null && !model.getAddress().isBlank()); 
    }

    @Override
    protected CrudService<FireStation> getService() {
        return fireStationService;
    }

    @Override
    protected boolean checkId(String[] id) {
        if ((id == null) || (id.length != 1) || id[0].isBlank()) {
            return false;
        } else {
            return true;
        }
    }
    
    // On invalide la suppression par défaut du CrudController en la surchargant car il faut soit supprimer par addresse, soit par station (la suppression par défaut ne fait que par adresse).
    @Override
    public ResponseEntity<?> deleteModelByUniqueKey(@PathVariable String param1,@PathVariable(required = false) String param2){
        log.info("suppression par adresse : " + this.getClass().getSimpleName() + " : fonctionnalité inexistante");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED) // 405
                .body("Non-existent functionality");
    }
    
    // suppression spéciale par adresse explicite => route = firestation/address/...
    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /address/{address}" => je n'attends comme paramètre que l'adresse.
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode.
    @DeleteMapping("/address/{address}")
    public ResponseEntity<?> deleteByAddress(@PathVariable String address){
        log.debug("appel de : /firestation/address/{address}");

        // le contrôleur doit d'abord vérifier les paramètres qui ont été reçus.
        if (!checkId(new String[]{address})) { // pour ne pas compliquer la fonction.
            log.info("suppression par adresse : " + this.getClass().getSimpleName() + " : paramètres incorrects");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("suppression : invalid parameters");
        }
        
        // presque ok.
        if (fireStationService.deleteByAddress(address)) {
            // ok.
            log.info("suppression par adresse : " + this.getClass().getSimpleName() + " : supression réussie");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression par adresse : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }
    
    // suppression spéciale par station explicite => route = firestation/station/...
    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /station/{station}" => je n'attends comme paramètre que la station.
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode.
    @DeleteMapping("/station/{station}")
    public ResponseEntity<?> deleteByStation(@PathVariable int station){
        log.debug("appel de : /firestation/station/{station}");

        // le contrôleur doit d'abord vérifier les paramètres qui ont été reçus.
        if (station <= 0) {
            log.info("suppression par station : " + this.getClass().getSimpleName() + " : paramètres incorrects");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("suppression : invalid parameters");
        }
        
        // presque ok.
        if (fireStationService.deleteByStation(station)) {
            // ok.
            log.info("suppression par station : " + this.getClass().getSimpleName() + " : supression réussie");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression par station : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }

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
    @GetMapping
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
    
}

