package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

/**
 * Data Transfer Object representing a flood alert, used to structure data related to households served by a specific station in case of flooding.
 * 
 */
/*
@Value better than @Data for DTOs because it generates all fields final, meaning they cannot be changed after initialization.
To avoid problems with modifying DTOs due to passing by reference in argument and function return.
*/
@Value
@JsonView(Views.Detailed.class) 
public class FloodAlertDTO {
    private int station;
    private List<FloodHouseoldDTO> households;
    
    // Even though Lombok generates a final field, a collection can be modified by the external reference => making the copy in the constructor is the recommended approach for immutable DTOs.
    public FloodAlertDTO(int station, List<FloodHouseoldDTO> households) {
        this.station = station;
        this.households = List.copyOf(households); // Defensive copy.
    }    
}
