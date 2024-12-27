package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.NonNull;

// Crud de base pour tous les modèles.
@Repository
public abstract class CrudRepository<ID, MODEL> {
    
    // Liste dans l'ordre car c'est une petite liste, je ne cherche pas l'optmisation en recherche.
    private List<MODEL> models = new ArrayList<>();

    // à implémenter par les classes filles et accessible uniquement par elles.
    protected abstract boolean isUnique(ID id, MODEL model);
        
    // pour récupérer la liste de tous les éléments du modèle.
    // retourne une copie : attention au passage par référence des objets.
    public List<MODEL> getModels() {
        return new ArrayList<>(models);  
    }

    // pour remplacer la liste de tous les éléments du modèle.
    // création d'une nouvelle liste : attention au passage par référence des objets.
    public void setModels(List<MODEL> newModels) {
        models = new ArrayList<>(newModels);  
    }

    // ajouter un modèle dans la liste de tous les éléments du modèle.
    // @NonNull gère l'exception si person est null.
    public void addModel(@NonNull MODEL model) {
        models.add(model);
    }

    // mise à jour du modèle avec la clef unique.
    public boolean updateModelByUniqueKey (ID id, @NonNull MODEL modelToUpdate) {
        for (MODEL model : models) {
            if (isUnique(id, model)) {
                models.set(models.indexOf(model), modelToUpdate);
                return true;
            }
        }
        return false;
    }
    
    // Suppression par clef unique avec lambda.
    public boolean deleteModelByUniqueKey(ID id) {
        return models.removeIf(model -> isUnique(id, model));
    }
    
    

}
