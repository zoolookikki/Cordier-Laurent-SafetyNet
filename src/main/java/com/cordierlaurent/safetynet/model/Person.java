package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
@SuppressWarnings("unused") // pour Eclipse.
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip; 
    private String phone;
    private String email;
}
