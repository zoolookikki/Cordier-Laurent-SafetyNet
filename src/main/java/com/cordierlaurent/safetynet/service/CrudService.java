package com.cordierlaurent.safetynet.service;

import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.repository.CrudRepository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
//Crud de base pour tous les services.
public abstract class CrudService<Model> {
    
    // à implémenter dans la classe fille : 
    // pour pouvoir parcourir tous les éléments du modèle contenu dans le repository concerné.
    // pour pouvoir appeler les fonctions de Crud de chaque repository concerné.
    protected abstract CrudRepository<Model> getRepository();
    // à implémenter dans la classe fille : on compare 2 modèles, chaque service qui héritera choisira sa façon de comparer les modèles pour décider qu'il est unique.
    protected abstract boolean isSameModel (Model model, Model modelToverify);
    
    // contrôle d'unicité dans le service (métier).
    public boolean isUnique(String[] id, @NonNull Model modelToVerify) {
        for (Model model : getRepository().getModels()) {
            if (isSameModel (model, modelToVerify)) {
                // nous sommes en modification (id non null car c'est la clef à modifier) et c'est nous même, donc ce n'est pas un futur doublon.
                if (id != null && getRepository().containsId(id, model)) {
                    continue;  
                }
                // Sinon, il y a un doublon.
                log.debug(this.getClass().getSimpleName() + " : doublon");
                return false;
            }
        }
        log.debug(this.getClass().getSimpleName() + " : unique");
        return true;
    }
    
    public void addModel(@NonNull Model modelToAdd) {
        getRepository().addModel(modelToAdd);
        log.debug(this.getClass().getSimpleName() + " : OK");
    }
    
    
    public boolean updateModelByUniqueKey (String[] id, @NonNull Model modelToUpdate) {
        return getRepository().updateModelByUniqueKey(id, modelToUpdate);
    }

    public boolean deleteModelByUniqueKey (String[] id) {
        return getRepository().deleteModelByUniqueKey(id);
    }
    
}

