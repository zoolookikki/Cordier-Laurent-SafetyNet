package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
public class MedicalRecord {

    @NotBlank(message = "Le prénom est obligatoire")    
    private String firstName;
    
    @NotBlank(message = "Le nom est obligatoire")    
    private String lastName;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$", message = "Le format de la date doit être mm/dd/yyyy")
    private String birthdate;
    
    @NotNull(message = "La liste des médicaments ne doit pas être nulle")
    private List<String> medications;
    
    @NotNull(message = "La liste des allergies ne doit pas être nulle")
    private List<String> allergies;
}
