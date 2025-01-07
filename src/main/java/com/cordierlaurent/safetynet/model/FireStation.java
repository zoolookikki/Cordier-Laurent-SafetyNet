package com.cordierlaurent.safetynet.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
public class FireStation {

    @NotBlank(message = "L'adresse est obligatoire")    
    private String address;

    @Positive(message = "Le numéro de station doit être positif")
    private int station;
    
}
