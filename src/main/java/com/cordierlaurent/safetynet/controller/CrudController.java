package com.cordierlaurent.safetynet.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.Util.ResponseEntityUtil;
import com.cordierlaurent.safetynet.service.CrudService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * Abstract REST controller for CRUD operations.
 * 
 * @param <Model> the type of model managed by this controller.
 */
@RestController
@Log4j2
@Validated
public abstract class CrudController <Model> {

    /**
     * Retourne the service associated with this controller.
     * 
     * This function is to be implemented in the child class, it allows you to call:
     * - the service concerned in order to ask it to verify the uniqueness. 
     * - the Crud functions of each service concerned.
     * 
     * @return the service.
     */
    protected abstract CrudService<Model> getService();

    /**
     * Creates a new entity.
     *
     * @param model the entity to add.
     * @return an HTTP response with the created entity or an error message.
     */
    // For @PostMapping, there is no (HTTP route to which the method must respond): the route must be defined in the child class with @RequestMapping.
    @PostMapping
    // ? instead of Model otherwise type mismatch error because here we return either Model or a String.
    public ResponseEntity<?> addModel(@Valid @RequestBody Model model) {
        log.debug(this.getClass().getSimpleName() + " : POST/addModel : "+ model);

        // verification of uniqueness by the service.
        if (!getService().isUnique(null,model)) {
            log.info("creation : already exists => " + model);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("creation : already exists");
        }
        
        log.info("creation : ok => " + model);
        getService().addModel(model);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 created => preferable to 200 ok.
                .body(model); // returns what was sent even to respect the standard.
    }
    
    
    /**
     * Updates an existing entity identified by a unique key.
     * 
     * "/{param1}", "/{param1}/{param2} : syntax to say that we expect 1 or 2 parameters on the url in addition to the Json in the body.
     * "/{param1}/", "/" : to capture cases with only the /
     *
     * @param param1 the first parameter of the unique key.
     * @param param2 the second parameter of the unique key.
     * @param model  the entity to update.
     * @return an HTTP response with the updated entity or an error message.
     */    
    @PutMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    // ? instead of Model otherwise type mismatch error because here we return either Model or a String.
    public ResponseEntity<?> updateModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2, 
            @Valid @RequestBody Model model){

        Optional<ResponseEntity<?>> validationResponse = ResponseEntityUtil.validateNotNullAndNotBlank(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        String[] id = new String[]{param1, param2};
            
        log.debug(this.getClass().getSimpleName() + " : PUT/updateModelByUniqueKey : key=" + param1 + "/" + param2  + " " + model);

        // for modification, we must check that the model we are going to update does not already exist.
        if (!getService().isUnique(id, model)) {
            log.info("update : another record already exists => " + model);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("update : another record already exists");
        }
        
        if (getService().updateModelByUniqueKey(id, model)) {
            // ok.
            log.info("update : ok => " + model);
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(model); // returns what was sent even to respect the standard.
        } else {
            // nok. 
            log.info("update : not found => " + model);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("update : not found");
        }
    }

    
    /**
     * Deletes an entity identified by a unique key.
     * 
     * "/{param1}", "/{param1}/{param2} : syntax to say that we expect 1 or 2 parameters on the url in addition to the Json in the body.
     * "/{param1}/", "/" : to capture cases with only the /
     *
     * @param param1 the first parameter of the unique key.
     * @param param2 the second parameter of the unique key.
     * @return an HTTP response confirming the deletion or an error message.
     */
    @DeleteMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    // ? instead of Model otherwise type mismatch error because here we return either Model or a String.
    public ResponseEntity<?> deleteModelByUniqueKey(
        @PathVariable(required = false) String param1,
        @PathVariable(required = false) String param2) {

        Optional<ResponseEntity<?>> validationResponse = ResponseEntityUtil.validateNotNullAndNotBlank(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        String[] id = new String[]{param1, param2};
            
        log.debug(this.getClass().getSimpleName() + " : DELETE/deleteModelByUniqueKey : key=" + param1 + '/' + param2);

        if (getService().deleteModelByUniqueKey(id)) {
            log.info("delete : ok");
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 better than 200
                    .build(); // we do not return a response, therefore a body null.
        } else {
            log.info("delete : not found");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("delete : not found");
        }
    }

}
