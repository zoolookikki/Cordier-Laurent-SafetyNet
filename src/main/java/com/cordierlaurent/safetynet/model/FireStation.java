package com.cordierlaurent.safetynet.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor 
public class FireStation {

    @NotBlank(message = "Address is required")    
    private String address;

    @Positive(message = "Station number must be positive")
    private int station;
}
