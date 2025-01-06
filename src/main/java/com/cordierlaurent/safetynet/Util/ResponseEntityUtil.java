package com.cordierlaurent.safetynet.Util;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ResponseEntityUtil {

    /*
    <Item> avant le retour car cela ne concernte que la méthode (d'autant plus qu'elle est static).
    on peut avoir en Item soit List ou Set => Collection est une interface parent commune donc OK.
    */
    public static <Item> ResponseEntity<?> response(Collection<Item> collection, String functionName){
        // la liste est vide => rien trouvé.
        if (collection.isEmpty()) {
            log.info("{} : aucun résultat trouvé", functionName);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 
                    .build(); // là, il ne faut peut être rien dire dans ce cas de figure => build : réponse sans body.
        }
        // il y a au moins un élément dans la liste.
        log.info("{} : {} résultat(s) trouvé(s)", functionName, collection.size());
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(collection); 
    }

}
