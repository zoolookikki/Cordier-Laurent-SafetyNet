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

/*
Le handler récupère tous les champs invalides et les messages associés, puis retourne un ResponseEntity 
*/
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    // pour récupérer les exceptions quand une validation échoue (@NotNull, @NotBlank, @Email) contrôler au niveau du body json.
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
    
    //pour récupérer les exceptions sur les @Validated qui active la validation sur les paramètres avec @PathVariable et @RequestParam.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));  // Concatène les erreurs en une seule chaîne

        log.debug("GlobalExceptionHandler : ConstraintViolationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    
    // pour récupérer les exceptions quand les types ne sont pas respectés (comme abcd sur les stations qui sont int)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();  // Nom du paramètre (ex: "station", "id", "address")
        
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valeur correcte";

        String errorMessage = String.format(
                "Le paramètre '%s' doit être de type '%s'.",
                parameterName,
                requiredType
        );

        log.debug("GlobalExceptionHandler : MethodArgumentTypeMismatchException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    
    // pour récupérer les exceptions quand les paramètres d'une requête de type http://....? sont absents.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        String errorMessage = "Le paramètre '" + paramName + "' est obligatoire.";
        
        log.debug("GlobalExceptionHandler : MissingServletRequestParameterException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    
    
}
