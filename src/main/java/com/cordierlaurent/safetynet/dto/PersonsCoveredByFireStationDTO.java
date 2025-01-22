package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

/**
 * Data Transfer Object representing the list of persons covered by a fire station, along with counts of adults and children.
 * 
 */
/*
@Value better than @Data for DTOs because it generates all fields final, meaning they cannot be changed after initialization.
To avoid problems with modifying DTOs due to passing by reference in argument and function return.
*/
@Value
@JsonView(Views.Address.class)
public class PersonsCoveredByFireStationDTO {
    @JsonProperty("persons")  // Renames the key in JSON serialization to "persons".
    private List<PersonInformationsDTO> personInformationsDTO;
    private int numberOfAdults;
    private int numberOfChildren;    
    
    // Even though Lombok generates a final field, a collection can be modified by the external reference => making the copy in the constructor is the recommended approach for immutable DTOs.
    public PersonsCoveredByFireStationDTO(List<PersonInformationsDTO> personInformationsDTO, int numberOfAdults, int numberOfChildren) {
        this.personInformationsDTO = List.copyOf(personInformationsDTO); // Defensive copy.
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }    
}
