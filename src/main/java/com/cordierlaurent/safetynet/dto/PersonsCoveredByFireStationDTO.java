package com.cordierlaurent.safetynet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonsCoveredByFireStationDTO {
    private List<PersonBasicInformationsDTO> personBasicInformationsDTO;
    private int numberOfAdults;
    private int numberOfChildren;    
}
