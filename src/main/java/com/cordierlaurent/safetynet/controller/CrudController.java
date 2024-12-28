package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public abstract class CrudController <ID, MODEL> {

    @Autowired
    private JsonDataRepository jsonDataRepository;

    // à implémenter dans la classe fille : on compare 2 modèles, chaque service qui héritera choisira sa façon de comparer les modèles pour décider qu'il est unique.
    protected abstract boolean checkModel (MODEL model);

    // à implémenter dans la classe fille : 
    // pour pouvoir appeler parcourir tous les éléments du modèle contenu dans le repository concerné.
    // pour pouvoir appeler les fonctions de Crud de chaque repository concerné.
    protected abstract CrudService<ID, MODEL> getService();
    
    
    // @PostMapping : mappe une requête HTTP POST à une méthode de contrôleur : ici en POST. 
    // ResponseEntity représente l'ensemble de la réponse HTTP envoyée au client (corps, status, entête http) : <MODEL> => objet générique retourné en Json.
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet générique MODEL (désérialisaion automatique : json -> MODEL).
    // ? au lieu de MODEL sinon erreur type mismatch car ici on renvoit soit MODEL soit un String.
    // ici pour @PostMapping je ne mets plus entre () la route HTTP à laquelle la méthode doit répondre : c'est juste un POST pour le moment, la route est à définir dans la classe fille avec @RequestMapping.
    @PostMapping
    public ResponseEntity<?> addModel(@RequestBody MODEL model) {

        // le contrôleur doit vérifier le contenu de la requête avant de le transmettre au service.
        if (!checkModel(model)) {
            log.info(this.getClass().getSimpleName() + " : entrée vide");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("missing fields");
        }
        
        // vérification de l'unicité par le service => logique métier.
        if (!getService().isUnique(model)) {
            log.info(this.getClass().getSimpleName() + " : pas unique");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("already exists.");
        }
        
        // c'est ok.
        log.info(this.getClass().getSimpleName() + " : tout est ok");
        getService().addModel(model);
        jsonDataRepository.save(); // je mets à jour le fichier json ici.
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 created => préférable à 200 ok.
                .body(model); // on retourne ce qui a été envoyé même si c'est identique ici pour respecter le standard.
    }

}
