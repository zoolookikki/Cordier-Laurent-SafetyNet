package com.cordierlaurent.safetynet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class FloodHouseoldDTO {
    private String address;
    private List<PersonHealthInformationsDTO> persons;
}
