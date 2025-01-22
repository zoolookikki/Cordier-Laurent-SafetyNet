package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@AllArgsConstructor 
public class MedicalRecord {

    @NotBlank(message = "First name is required")    
    private String firstName;
    
    @NotBlank(message = "Name is required")    
    private String lastName;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$", message = "The date format must be mm/dd/yyyy")
    private String birthdate;
    
    @NotNull(message = "The list of medications must not be null")
    private List<String> medications;
    
    @NotNull(message = "The list of allergies must not be null")
    private List<String> allergies;
}
