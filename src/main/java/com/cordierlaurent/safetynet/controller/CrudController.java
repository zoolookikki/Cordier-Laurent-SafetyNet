package com.cordierlaurent.safetynet.controller;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
//@Validated Active la validation sur les paramètres avec @PathVariable et @RequestParam (voir @NotBlank etc... dans les paramètres de chaque méthode)
@Validated
public abstract class CrudController <Model> {

    @Autowired
    protected JsonDataRepository jsonDataRepository;

    // à implémenter dans la classe fille : 
    // pour pouvoir appeler le service concerné pour lui demander de vérifier l'unicité.
    // pour pouvoir appeler les fonctions de Crud de chaque service concerné.
    protected abstract CrudService<Model> getService();
    
    protected Optional<ResponseEntity<?>> validateNotNullAndNotBlank(String param1, String param2) {
        if (param1 == null || param2 == null || param1.isBlank() || param2.isBlank()) {
            return Optional.of(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Les 2 paramètres sont obligatoires"));
        }
        return Optional.empty(); // OK
    }    
    
    // @PostMapping : mappe une requête HTTP POST à une méthode de contrôleur : création. 
    // ResponseEntity représente l'ensemble de la réponse HTTP envoyée au client (corps, status, entête http) : <Model> => objet générique retourné en Json.
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet générique Model (désérialisaion automatique : json -> Model).
    // @Valid : pour valider tout ce qui a été déclaré comme à contrôler dans le modèle (@NotNull, @NotBlank etc...)    
    // ? au lieu de Model sinon erreur type mismatch car ici on renvoit soit Model soit un String.
    // ici pour @PostMapping je ne mets plus entre () la route HTTP à laquelle la méthode doit répondre : c'est juste un POST pour le moment, la route est à définir dans la classe fille avec @RequestMapping.
    @PostMapping
    public ResponseEntity<?> addModel(@Valid @RequestBody Model model) {
        log.debug(this.getClass().getSimpleName() + " : POST/addModel : "+ model);

        // vérification de l'unicité par le service => logique métier.
        if (!getService().isUnique(null,model)) {
            log.info("creation : " + model + " ==> doublon");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("creation : already exists");
        }
        
        // c'est ok.
        log.info("creation : " + model + " ==> ok");
        getService().addModel(model);
        jsonDataRepository.save(); // je mets à jour le fichier json ici.
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 created => préférable à 200 ok.
                .body(model); // on retourne ce qui a été envoyé même si c'est identique ici pour respecter le standard.
    }
    
    // @PutMapping : mappe une requête HTTP PUT à une méthode de contrôleur : mise à jour.
    // /{param1}", "/{param1}/{param2} : syntaxe pour dire que j'attends 1 ou 2 paramètres sur l'url en plus du Json dans le body.
    // "/{param1}/", "/" : pour capturer les cas avec uniquement le /
    // $PatchVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode, le 2 ème n'étant pas requis (requis par défaut).
    // @Valid : pour valider tout ce qui a été déclaré comme à contrôler dans le modèle (@NotNull, @NotBlank etc...)    
    // @RequestBody : pour lier automatiquement le corps de la requête HTTP (JSON, XML, etc.) à l'objet générique Model (désérialisaion automatique : json -> Model).
    @PutMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
    public ResponseEntity<?> updateModelByUniqueKey(
            @PathVariable(required = false) String param1,
            @PathVariable(required = false) String param2, 
            @Valid @RequestBody Model model){

        Optional<ResponseEntity<?>> validationResponse = validateNotNullAndNotBlank(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // construction de l'id
        String[] id = new String[]{param1, param2};
            
        log.debug(this.getClass().getSimpleName() + " : PUT/updateModelByUniqueKey : key=" + param1 + "/" + param2  + " " + model);

        // attention, pour la modification, il faut vérifier que le modèle que l'on va mettre à jour n'existe pas déjà (sauf moi même...).
        // vérification de l'unicité par le service => logique métier.
        if (!getService().isUnique(id, model)) {
            log.info("modification : " + model + " ==> doublon");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 
                    .body("modification : another record already exists");
        }
        
        // c'est presque ok.
        if (getService().updateModelByUniqueKey(id, model)) {
            // ok.
            log.info("modification : " + model  + " ==> ok");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(model); // on retourne ce qui a été envoyé même si c'est identique ici pour respecter le standard.
        } else {
            // nok. 
            log.info("modification : " + model + " ==> non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("modification : not found");
        }
    }
    
    // @DeleteMapping : mappe une requête HTTP DELETE à une méthode de contrôleur : suppression.
    // /{param1}", "/{param1}/{param2} : syntaxe pour dire que j'attends 1 ou 2 paramètres sur l'url en plus du Json dans le body.
    // "/{param1}/", "/" : pour capturer les cas avec uniquement le /
    // $PathVariable : extrait les paramètres de la requête HTTP et les transmet en tant que paramètres à la méthode, le 2 ème n'étant pas requis (requis par défaut) ==> attention param2 est null si absent
    @DeleteMapping({"/{param1}", "/{param1}/{param2}", "/{param1}/", "/"})
//    @DeleteMapping({"/{param1}/{param2}"})
    public ResponseEntity<?> deleteModelByUniqueKey(
        @PathVariable(required = false) String param1,
        @PathVariable(required = false) String param2) {

        Optional<ResponseEntity<?>> validationResponse = validateNotNullAndNotBlank(param1, param2);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        // construction de l'id
        String[] id = new String[]{param1, param2};
            
        log.debug(this.getClass().getSimpleName() + " : DELETE/deleteModelByUniqueKey : key=" + param1 + '/' + param2);

        if (getService().deleteModelByUniqueKey(id)) {
            // ok.
            log.info("suppression : " + Arrays.toString(id) + " ==> ok");
            jsonDataRepository.save(); // je mets à jour le fichier json ici.
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204 mieux que 200
                    .build(); // on ne retourne pas de réponse, soit un body null.
        } else {
            // nok. 
            log.info("suppression : " + Arrays.toString(id) + " ==> non trouvé ");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .body("suppression : not found");
        }
    }

}
