package com.cordierlaurent.safetynet.exception;

// pour que ce soit plus clair au niveau des exceptions pour json (j'aurai pu utiliser IllegalArgumentException) => customisable si besoin.
public class JsonFileException extends RuntimeException {

    // si utilisation de la sérialisation (application distribuée / sauvegarde), ce qui n'est pas mon cas ici, mais évite le warning sous Eclipse.
    private static final long serialVersionUID = 1L; 
    
    public JsonFileException(String message) {
        super(message);
    }

    public JsonFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
