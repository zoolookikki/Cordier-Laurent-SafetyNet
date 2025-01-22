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

import com.cordierlaurent.safetynet.Util.ResponseEntityUtil;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * Controller responsible for handling HTTP requests related to FireStation entities.
 * 
 * This controller extends CrudController to reuse generic CRUD functionalities and provides additional validation logic specific to FireStation entities.
 * 
 */
@RestController
@Log4j2
// The base route for this controller.
@RequestMapping("/firestation")
@Validated
public class FireStationController extends CrudController<FireStation>{

    @Autowired
    private FireStationService fireStationService;
    
    /**
     * Provides the specific service for abstract CrudController.
     *
     * @return The instance of the service.
     */
    @Override
    protected CrudService<FireStation> getService() {
        return fireStationService;
    }

    /**
     * Validates the station number parameter to ensure it is a positive integer.
     * 
     * @param param2 The station number as a string.
     * @return an Optional containing a ResponseEntity with an error message if validation fails, or an empty if validation passes.
     */
    private Optional<ResponseEntity<?>> validateStationNumber(String param2) {
        try {
            int station = Integer.parseInt(param2);
            if (station <= 0) {
                return Optional.of(ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("The station number must be greater than 0."));
            }
        } catch (NumberFormatException e) {
            return Optional.of(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The station number must be a valid integer."));
        }
        return Optional.empty(); // OK
    }    
    
    /**
     * Validates the FireStation parameters, ensuring that:
     * <ul>
     *   <li>`param1` and `param2` are not null or blank.</li>
     *   <li>`param2` (station number) is a valid positive integer.</li>
     * </ul>
     * 
     * @param param1 The first parameter (address).
     * @param param2 The second parameter (station number).
     * @return an Optional containing a ResponseEntity with an error message if validation fails, or an empty if validation passes.
     */
    private Optional<ResponseEntity<?>> validateFireStationParameters(String param1, String param2) {
        return ResponseEntityUtil.validateNotNullAndNotBlank(param1, param2)
            .or(() -> validateStationNumber(param2)); 
    }    
    
    /**
     * Updates a FireStation entity identified by the provided unique key.
     * 
     * The unique key consists of `param1` (address) and `param2` (station number).
     *
     * "/{param1}", "/{param1}/{param2} : syntax to say that we expect 1 or 2 parameters on the url in addition to the Json in the body.
     * "/{param1}/", "/" : to capture cases with only the /
     *
     * @param param1 The address of the FireStation.
     * @param param2 The station number of the FireStation.
     * @param model  The FireStation containing updated data.
     * @return a ResponseEntity containing the updated entity or an error message if validation fails.
     */
    @Override
    @PutMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    // ? instead of Model otherwise type mismatch error because here we return either Model or a String.
    public ResponseEntity<?> updateModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2, 
            @Valid @RequestBody FireStation model) {

        Optional<ResponseEntity<?>> validationResponse = validateFireStationParameters(param1, param2);
        // It's not OK !
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // Call the parent method to use common update logic.
        return super.updateModelByUniqueKey(param1, param2, model);
    }
    
    /**
     * Deletes a FireStation entity identified by the provided unique key.
     * 
     * The unique key consists of `param1` (address) and `param2` (station number).
     *
     * "/{param1}", "/{param1}/{param2} : syntax to say that we expect 1 or 2 parameters on the url in addition to the Json in the body.
     * "/{param1}/", "/" : to capture cases with only the /
     *
     * @param param1 The address of the FireStation.
     * @param param2 The station number of the FireStation.
     * @return a ResponseEntity indicating the result of the delete operation.
     */
    @Override
    @DeleteMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    public ResponseEntity<?> deleteModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2) {
            
        Optional<ResponseEntity<?>> validationResponse = validateFireStationParameters(param1, param2);
        // It's not OK !
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // Call the parent method to use common update logic.
        return super.deleteModelByUniqueKey(param1, param2);
    }    
    
}

