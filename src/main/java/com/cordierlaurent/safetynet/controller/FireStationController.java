package com.cordierlaurent.safetynet.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
// La route est définie ici dans la classe fille.
@RequestMapping("/firestation")
//@Validated Active la validation sur les paramètres avec @PathVariable et @RequestParam (voir @NotBlank etc... dans les paramètres de chaque méthode)
@Validated
public class FireStationController extends CrudController<FireStation>{

    @Autowired
    private FireStationService fireStationService;
    
    @Override
    protected CrudService<FireStation> getService() {
        return fireStationService;
    }

    // On invalide la suppression par défaut du CrudController en la surchargant car il faut soit supprimer par addresse, soit par station (la suppression par défaut ne fait que par adresse).
    @Override
    public ResponseEntity<?> deleteModelByUniqueKey(@PathVariable(required = false) String param1,@PathVariable(required = false) String param2){
        log.info("suppression par adresse : fonctionnalité inexistante");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED) // 405
                .body("Non-existent functionality");
    }
    
    // suppression spéciale par adresse explicite => route = firestation/address/...
    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /address/{address}" : je n'attends comme paramètre que l'adresse.
    // /address/{address}/ : pour capturer les cas avec uniquement le /
    // $PathVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode 
    @DeleteMapping({"/address/{address}", "/address/{address}/"})
    public ResponseEntity<?> deleteByAddress(
            @PathVariable(required = false)
            @NotBlank(message = "L'adresse est obligatoire") String address){

        log.debug("DELETE/deletedeleteByAddress : key=" + address);
        
        // presque ok.
        if (fireStationService.deleteByAddress(address)) {
            // ok.
            log.info("suppression par adresse : " + address + " ==> ok");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression par adresse : " + address + " ==> non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }
    
    // suppression spéciale par station explicite => route = firestation/station/...
    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /station/{station}" => je n'attends comme paramètre que la station.
    // /address/{station}/ : pour capturer les cas avec uniquement le /
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode.
    @DeleteMapping({"/station/{station}", "/station/{station}/"})
    public ResponseEntity<?> deleteByStation(
            @PathVariable(required = false)
            @Min(value = 1, message = "Le numéro de station doit être supérieur à 0") int station){

        log.debug("DELETE/deleteByStation : key=" + station);
        
        // presque ok.
        if (fireStationService.deleteByStation(station)) {
            // ok.
            log.info("suppression par station : " + station + " ==> ok");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression par station : " + station + " ==> non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }
   
}

