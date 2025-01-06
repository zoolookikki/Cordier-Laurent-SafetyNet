package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.NonNull;

// Crud de base pour tous les modèles.
@Repository
public abstract class CrudRepository<Model> {
    
    // Liste dans l'ordre car c'est une petite liste, je ne cherche pas l'optmisation en recherche.
    // protected au lieu de private car une des filles en a besoin pour la manipuler (suppression par station de FireStationRepository)
    protected List<Model> models = new ArrayList<>();

    // à implémenter par les classes filles.
    // public car j'en ai besoin dans le CrudService pour vérifier le cas de la mise à jour (voir isUnique).
    public abstract boolean containsId(String[] id, Model model);
        
    // pour récupérer la liste de tous les éléments du modèle.
    // retourne une copie : attention au passage par référence des objets.
    public List<Model> getModels() {
        return new ArrayList<>(models);  
    }

    // pour remplacer la liste de tous les éléments du modèle.
    // création d'une nouvelle liste : attention au passage par référence des objets.
    public void setModels(List<Model> newModels) {
        models = new ArrayList<>(newModels);  
    }

    // ajouter un modèle dans la liste de tous les éléments du modèle.
    // @NonNull gère l'exception si person est null.
    public void addModel(@NonNull Model model) {
        models.add(model);
    }

    // mise à jour du modèle avec la clef unique.
    public boolean updateModelByUniqueKey (String[] id, @NonNull Model modelToUpdate) {
        for (Model model : models) {
            if (containsId(id, model)) {
                models.set(models.indexOf(model), modelToUpdate);
                return true;
            }
        }
        return false;
    }
    
    // Suppression par clef unique avec lambda.
    public boolean deleteModelByUniqueKey(String[] id) {
        return models.removeIf(model -> containsId(id, model));
    }

}
