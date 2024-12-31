package com.cordierlaurent.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class PersonBasicInformationsDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // pour un test.
    //private String birthdate;
}
