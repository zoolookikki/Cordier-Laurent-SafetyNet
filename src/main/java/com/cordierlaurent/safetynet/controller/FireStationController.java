package com.cordierlaurent.safetynet.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;

import jakarta.validation.Valid;
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

    private Optional<ResponseEntity<?>> validateStationNumber(String param2) {
        try {
            int station = Integer.parseInt(param2);
            if (station <= 0) {
                return Optional.of(ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Le numéro de station doit être supérieur à 0."));
            }
        } catch (NumberFormatException e) {
            return Optional.of(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Le numéro de station doit être un entier valide."));
        }
        return Optional.empty(); // OK
    }    
    

    private Optional<ResponseEntity<?>> validateFireStationParameters(String param1, String param2) {
        return validateNotNullAndNotBlank(param1, param2)
            .or(() -> validateStationNumber(param2)); 
    }    
    
    // pour vérification SANS les annotations du numéro de station qui est un int.

    @Override
    @PutMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    public ResponseEntity<?> updateModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2, 
            @Valid @RequestBody FireStation model) {

        Optional<ResponseEntity<?>> validationResponse = validateFireStationParameters(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // Appeler la méthode parente pour utiliser la logique de mise à jour commune
        return super.updateModelByUniqueKey(param1, param2, model);
    }
    
    @Override
    @DeleteMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    public ResponseEntity<?> deleteModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2) {
            
        Optional<ResponseEntity<?>> validationResponse = validateFireStationParameters(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // Appeler la méthode parente pour utiliser la logique de suppression commune
        return super.deleteModelByUniqueKey(param1, param2);
    }    
    
}

