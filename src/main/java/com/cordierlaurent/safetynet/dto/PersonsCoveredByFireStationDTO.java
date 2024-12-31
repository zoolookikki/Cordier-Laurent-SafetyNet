package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonsCoveredByFireStationDTO {
    @JsonProperty("persons")  // Change le nom de la clé.
    private List<PersonBasicInformationsDTO> personBasicInformationsDTO;
    private int numberOfAdults;
    private int numberOfChildren;    
}
