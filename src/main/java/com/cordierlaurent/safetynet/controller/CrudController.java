package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public abstract class CrudController <MODEL> {

    @Autowired
    protected JsonDataRepository jsonDataRepository;

    // à implémenter dans la classe fille : on vérifie le modèle reçu via le contrôleur, chaque contrôleur qui héritera choisira sa façon de vérifier le contenu du Json.
    protected abstract boolean checkModel (MODEL model);
    
    // à implémenter dans la classe fille : 
    // pour pouvoir appeler le service concerné pour lui demander de vérifier l'unicité.
    // pour pouvoir appeler les fonctions de Crud de chaque service concerné.
    protected abstract CrudService<MODEL> getService();
    
    // à implémenter dans la classe fille : on vérifie les paramètres reçus via le contrôleur, chaque contrôleur qui héritera choisira sa façon de vérifier les paramètres.
    protected abstract boolean checkId (String[] id);
    
    // @PostMapping : mappe une requête HTTP POST à une méthode de contrôleur : création. 
    // ResponseEntity représente l'ensemble de la réponse HTTP envoyée au client (corps, status, entête http) : <MODEL> => objet générique retourné en Json.
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet générique MODEL (désérialisaion automatique : json -> MODEL).
    // ? au lieu de MODEL sinon erreur type mismatch car ici on renvoit soit MODEL soit un String.
    // ici pour @PostMapping je ne mets plus entre () la route HTTP à laquelle la méthode doit répondre : c'est juste un POST pour le moment, la route est à définir dans la classe fille avec @RequestMapping.
    @PostMapping
    public ResponseEntity<?> addModel(@RequestBody MODEL model) {
        log.debug("appel de : POST/addModel");

        if (!checkModel(model)) {
            // le contrôleur doit vérifier le contenu de la requête avant de le transmettre au service.
            log.info("creation : " + this.getClass().getSimpleName() + " : objet Json incorrect");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("creation : missing fields");
        }
        
        // vérification de l'unicité par le service => logique métier.
        if (!getService().isUnique(null,model)) {
            log.info("creation : " + this.getClass().getSimpleName() + " : pas unique");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("creation : already exists");
        }
        
        // c'est ok.
        log.info("creation : " + this.getClass().getSimpleName() + " : tout est ok");
        getService().addModel(model);
        jsonDataRepository.save(); // je mets à jour le fichier json ici.
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 created => préférable à 200 ok.
                .body(model); // on retourne ce qui a été envoyé même si c'est identique ici pour respecter le standard.
    }
    
    // @PutMapping : mappe une requête HTTP PUT à une méthode de contrôleur : mise à jour.
    // /{param1}", "/{param1}/{param2} : syntaxe pour dire que j'attends 1 ou 2 paramètres sur l'url en plus du Json dans le body.
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode, le 2 ème n'étant pas requis (requis par défaut).
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet générique MODEL (désérialisaion automatique : json -> MODEL).
    @PutMapping({"/{param1}", "/{param1}/{param2}"})
    public ResponseEntity<?> updateModelByUniqueKey(@PathVariable String param1,@PathVariable(required = false) String param2, @RequestBody MODEL model){
        log.debug("appel de : PUT/updateModelByUniqueKey");

        // Construction de l'id en fonction du nombre de paramètres.
        String[] id = (param2 != null) ? new String[]{param1, param2} : new String[]{param1};
        
        // le contrôleur doit d'abord vérifier les paramètres qui ont été reçus.
        if (!checkId(id)) {
            log.info("modification : " + this.getClass().getSimpleName() + " : paramètres incorrects");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("modification : invalid parameters");
        }
        
        // comme pour la création, le contrôleur doit vérifier le contenu de la requête avant de le transmettre au service.
        if (!checkModel(model)) {
            log.info("modification : " + this.getClass().getSimpleName() + " : objet Json incorrect");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("modification : missing fields");
        }
        
        // attention, pour la modification, il faut vérifier que le modèle que l'on va mettre à jour n'existe pas déjà (sauf moi même...).
        // vérification de l'unicité par le service => logique métier.
        if (!getService().isUnique(id, model)) {
            log.info("modification : " + this.getClass().getSimpleName() + " : doublon");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("modification : another record already exists");
        }
        
        // c'est presque ok.
        if (getService().updateModelByUniqueKey(id, model)) {
            // ok.
            log.info("modification : " + this.getClass().getSimpleName() + " : mise à jour réussie");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(model); // on retourne ce qui a été envoyé même si c'est identique ici pour respecter le standard.
        } else {
            // nok. 
            log.info("modification : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("modification : not found");
        }
    }

    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /{param1}", "/{param1}/{param2} : syntaxe pour dire que j'attends 1 ou 2 paramètres sur l'url en plus du Json dans le body.
    // $PathVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode, le 2 ème n'étant pas requis (requis par défaut).
    @DeleteMapping({"/{param1}", "/{param1}/{param2}"})
    public ResponseEntity<?> deleteModelByUniqueKey(@PathVariable String param1,@PathVariable(required = false) String param2){
        log.debug("appel de : DELETE/deleteModelByUniqueKey");

        // Construction de l'id en fonction du nombre de paramètres.
        String[] id = (param2 != null) ? new String[]{param1, param2} : new String[]{param1};
        
        // le contrôleur doit d'abord vérifier les paramètres qui ont été reçus.
        if (!checkId(id)) {
            log.info("suppression : " + this.getClass().getSimpleName() + " : paramètres incorrects");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 
                    .body("suppression : invalid parameters");
        }
        
        if (getService().deleteModelByUniqueKey(id)) {
            // ok.
            log.info("suppression : " + this.getClass().getSimpleName() + " : supression réussie");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression : " + this.getClass().getSimpleName() + " : non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }
    
}
