package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

/**
 * Data Transfer Object representing fire-related information.
 * 
 */
/*
@Value better than @Data for DTOs because it generates all fields final, meaning they cannot be changed after initialization.
To avoid problems with modifying DTOs due to passing by reference in argument and function return.
*/
@Value
@JsonView(Views.Detailed.class)
public class FireDTO {
    int station;
    List<PersonInformationsDTO> persons;
    
    // Even though Lombok generates a final field, a collection can be modified by the external reference => making the copy in the constructor is the recommended approach for immutable DTOs.
    public FireDTO(int station, List<PersonInformationsDTO> persons) {
        this.station = station;
        this.persons = List.copyOf(persons); // Defensive copy.
    }    
}
