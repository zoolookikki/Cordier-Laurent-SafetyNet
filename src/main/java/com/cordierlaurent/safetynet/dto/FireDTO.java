package com.cordierlaurent.safetynet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FireDTO {
    int station;
    List<PersonHealthInformationsDTO> persons;
}
