package com.cordierlaurent.safetynet.Util;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ResponseEntityUtil {

 // Constructeur privé pour empêcher l'instanciation
    private ResponseEntityUtil() {
    }
    
    /**
     * Response for a collection.
     * @param collection Depending on the size of the collection, the log response is different.
     * @param message Message to display in the log.
     * @return ResponseEntity Containing the collection or an empty list if it is empty.
     */
    public static ResponseEntity<?> response(Collection<?> collection, String logMessage) {
        if (collection.isEmpty()) {
            log.info("{} : aucun résultat trouvé", logMessage);
        } else {
            log.info("{} : {} résultat(s) trouvé(s)", logMessage, collection.size());
        }
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(collection);
    }
    
    /**
     * Response for a DTO object.
     * @param dto DTO to convert into json.
     * @param collection Depending on the size of the collection, the log response is different.
     * @param message Message to display in the log.
     * @return ResponseEntity Containing the DTO or an empty list if the collection is empty.
     */
    public static ResponseEntity<?> response(Object dto, Collection<?> collection, String logMessage) {
        if (collection.isEmpty()) {
            log.info("{} : aucun résultat trouvé", logMessage);
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(Collections.emptyList());
        } else {
            log.info("{} : {} résultat(s) trouvé(s)", logMessage, collection.size());
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(dto);
        }
    }    
    
    public static Optional<ResponseEntity<?>> validateNotNullAndNotBlank(String param1, String param2) {
        if (param1 == null || param2 == null || param1.isBlank() || param2.isBlank()) {
            return Optional.of(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Les 2 paramètres sont obligatoires"));
        }
        return Optional.empty(); // OK
    }    
    
}
