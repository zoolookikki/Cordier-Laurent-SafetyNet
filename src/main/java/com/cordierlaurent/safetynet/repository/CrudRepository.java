package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

/**
 * Abstract base repository providing basic CRUD operations for all models.
 * 
 * @param <Model> The type of the model this repository will manage.
 */
@Repository
public abstract class CrudRepository<Model> {
    
    /**
     * List of models managed by the repository.
     * 
     */
    // protected instead of private because one of the child class needs it to handle it (delete by station from FireStationRepository)
    protected List<Model> models = new ArrayList<>();

    /**
     * Checks whether a given model matches a unique identifier.
     * 
     * <p>This method must be implemented by subclasses to provide logic for determining whether a given model corresponds to the provided unique identifier.</p>
     * 
     * @param id The unique identifier to check.
     * @param model The model to compare.
     * @return true if the model matches the identifier, false otherwise.
     */
    // public because i need it in the CrudService to check the update case (see isUnique).
    public abstract boolean containsId(String[] id, Model model);
        
    /**
     * Retrieves the list of all models.
     * 
     * <p>A new copy of the list is returned to avoid modification by reference.</p>
     * 
     * @return A copy of the list of all models.
     */
    public List<Model> getModels() {
        return new ArrayList<>(models);  
    }

    /**
     * Replaces the current list of models with a new list.
     * 
     * <p>A defensive copy of the new list is created to prevent external modifications by reference.</p>
     * 
     * @param newModels The new list of models.
     * @throws NullPointerException if newModels is null.
     */
    public void setModels(List<Model> newModels) {
        Objects.requireNonNull(newModels, "newModels cannot be null");
        models = new ArrayList<>(newModels);  
    }

    /**
     * Adds a new model to the list of models.
     * 
     * @param model The model to add.
     * @throws NullPointerException if model is null.
     */
    public void addModel(Model model) {
        Objects.requireNonNull(model, "model cannot be null");
        models.add(model);
    }

    /**
     * Updates a model in the list based on its unique key.
     * 
     * <p>If a model with the specified identifier exists, it is replaced by the provided model</p>
     * 
     * @param id The unique identifier of the model to update.
     * @param modelToUpdate The new model to replace the existing one.
     * @return true if the model was updated, false if no matching model was found.
     * @throws NullPointerException if id or modelToUpdate is null.
     */
    public boolean updateModelByUniqueKey (String[] id, Model modelToUpdate) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(modelToUpdate, "modelToUpdate cannot be null");
        for (Model model : models) {
            if (containsId(id, model)) {
                models.set(models.indexOf(model), modelToUpdate);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Deletes a model from the list based on its unique key.
     * 
     * @param id The unique identifier of the model to delete.
     * @return true if a model was deleted, false if no matching model was found.
     * @throws NullPointerException if id is null.
     */
    public boolean deleteModelByUniqueKey(String[] id) {
        Objects.requireNonNull(id, "id cannot be null");
        return models.removeIf(model -> containsId(id, model));
    }

}
