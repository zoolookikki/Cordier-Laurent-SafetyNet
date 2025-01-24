package com.cordierlaurent.safetynet.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Abstract service class providing basic CRUD operations for models.
 * 
 * @param <Model> The type of the model managed by the service.
 */
@Service
@Log4j2
public abstract class CrudService<Model> {
    
    @Autowired
    protected JsonDataRepository jsonDataRepository;

    
    /**
     * Retrieves the repository associated with the model type.
     * 
     * <p>This method must be implemented by child classes to provide access to the specific repository handling the desired model type.</p>
     * 
     * @return the repository instance managing the model type.
     */
    protected abstract CrudRepository<Model> getRepository();

    /**
     * Compares two models to determine if they are the same.
     * 
     * <p>This method must be implemented by child classes to define how models should be compared for uniqueness. The logic may vary depending on the requirements of the model.</p>
     * 
     * @param model the existing model.
     * @param modelToVerify the model to verify against the existing one.
     * @return true if the models are considered the same, false otherwise.
     */    
    protected abstract boolean isSameModel (Model model, Model modelToVerify);
    
    /**
     * Validates that the id is non-null and consists of exactly two strings.
     * 
     * @param id the id to validate.
     * @throws NullPointerException if the id is null.
     * @throws IllegalArgumentException if the id does not consist of two strings.
     */
    private void validId (String[] id) {
        Objects.requireNonNull(id, "id cannot be null");
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be composed of an array of 2 strings");
        }
    }

    /**
     * Check if a model is unique before creation or modification.
     * 
     * <p>If the model is being updated (indicated by a non null id), it ensures that the model being updated is not considered a duplicate of itself.</p>
     * 
     * @param id the unique identifier of the model being updated (can be null for a creation).
     * @param modelToVerify the model to check for uniqueness.
     * @return true if the model is unique, false otherwise.
     * @throws NullPointerException if modelToVerify is null.
     */
    public boolean isUnique(String[] id, Model modelToVerify) {
        // warning, here, id can be null when creation.
        Objects.requireNonNull(modelToVerify, "modelToVerify cannot be null");
        
        for (Model model : getRepository().getModels()) {
            if (isSameModel (model, modelToVerify)) {
                // we are in modification (id not null because it is the key to modify) and it is us, so it is not a future duplicate.
                if (id != null && getRepository().containsId(id, model)) {
                    continue;  
                }
                // Otherwise, there is a duplicate.
                log.debug(this.getClass().getSimpleName() + " : duplicate");
                return false;
            }
        }
        log.debug(this.getClass().getSimpleName() + " : unique");
        return true;
    }
    
    /**
     * Adds a new model to the repository.
     * 
     * @param modelToAdd the model to add.
     * @throws NullPointerException if modelToAdd is null.
     */
    public void addModel(Model modelToAdd) {
        Objects.requireNonNull(modelToAdd, "modelToAdd cannot be null");
        getRepository().addModel(modelToAdd);
        jsonDataRepository.save();
        log.debug(this.getClass().getSimpleName() + " : OK");
    }
    
    
    /**
     * Updates a model in the repository based on its unique key.
     * 
     * @param id the unique identifier of the model to update.
     * @param modelToUpdate the updated model data.
     * @return true if the update was successful, false otherwise.
     * @throws NullPointerException if id or modelToUpdate is null.
     * @throws IllegalArgumentException if the id is invalid.
     */
    public boolean updateModelByUniqueKey (String[] id, Model modelToUpdate) {
        validId(id);
        Objects.requireNonNull(modelToUpdate, "modelToUpdate cannot be null");
        if (getRepository().updateModelByUniqueKey(id, modelToUpdate)) {
            jsonDataRepository.save();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a model from the repository based on its unique key.
     * 
     * @param id the unique identifier of the model to delete.
     * @return true if the deletion was successful,false otherwise.
     * @throws NullPointerException if id is null.
     * @throws IllegalArgumentException if the id is invalid.
     */
    public boolean deleteModelByUniqueKey (String[] id) {
        validId(id);
        if (getRepository().deleteModelByUniqueKey(id)) {
            jsonDataRepository.save();
            return true;
        } else {
            return false;
        }
    }
    
}

