package com.cordierlaurent.safetynet.exception;

/**
 * Custom exception to handle errors related to JSON file operations.
 *
 * <p>This exception is a subclass of RuntimeException and is intended to provide clarity when dealing with JSON file-related issues in the application. 
 * It is designed to be more specific than a general IllegalArgumentException and can be customized further if necessary.</p>
 *
 * <p>While the class does not currently utilize serialization, the serialVersionUID is defined to avoid warnings in tools like Eclipse when serialization is applied.</p>
 */
public class JsonFileException extends RuntimeException {

    private static final long serialVersionUID = 1L; 
    
    public JsonFileException(String message) {
        super(message);
    }

    public JsonFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
