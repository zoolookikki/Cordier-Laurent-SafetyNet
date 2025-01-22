package com.cordierlaurent.safetynet.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for managing application-level exceptions and providing user-friendly error responses.
 *
 * This class uses Spring's @RestControllerAdvice to intercept exceptions across all controllers and return appropriate HTTP responses with meaningful error messages.
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
    * To catch exceptions when validation on a request body fails (@NotNull, @NotBlank, @Email).
    *
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        log.debug("GlobalExceptionHandler : MethodArgumentNotValidException");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
    
    /**
     * To catch exceptions on @Validated which enables validation on parameters with @PathVariable and @RequestParam.
     *
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));  // Concatenate errors into a single string

        log.debug("GlobalExceptionHandler : ConstraintViolationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    
    /**
     * To catch exceptions when types are not respected (like abcd on stations that are int)
     *
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();  // Nom du paramètre (ex: "station", "id", "address")
        
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "bad value";

        String errorMessage = String.format(
                "The '%s' parameter must be of type '%s'.",
                parameterName,
                requiredType
        );

        log.debug("GlobalExceptionHandler : MethodArgumentTypeMismatchException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    
    /**
     * To recover exceptions when the parameters of a request like http://....? are absent.
     *
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        String errorMessage = "The parameter '" + paramName + "' is required.";
        
        log.debug("GlobalExceptionHandler : MissingServletRequestParameterException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    /**
     * To avoid the basic internal server error => cleaner, only the essentials are displayed for the client, no disclosure of information.
     *
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        
        log.error("Null pointer exception: ", ex);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal error : " + ex.getMessage());
    }
    
    /**
     * To avoid the basic internal server error => cleaner, only the essentials are displayed for the client, no disclosure of information.
     *
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        
        log.error("IllegalArgumentException occurred: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal error / invalid input : " + ex.getMessage());
    }
    
    /**
     * To catch JsonFileException to return an HTTP 500 response with a message for the customer without disclosing information.
     *
     */
    @ExceptionHandler(JsonFileException.class)
    public ResponseEntity<?> handleJsonFileException(JsonFileException ex) {

        log.error("Error in JSON file operation: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal error / invalid JSON file : " + ex.getMessage());
    }
    
}
