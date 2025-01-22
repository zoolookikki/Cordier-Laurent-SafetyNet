package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

/**
 * Data Transfer Object representing detailed information about a person.
 * 
 */
/*
@Value better than @Data for DTOs because it generates all fields final, meaning they cannot be changed after initialization.
To avoid problems with modifying DTOs due to passing by reference in argument and function return.
*/
@Value
//Default view: shows only "Basic" fields
@JsonView(Views.Basic.class)
public class PersonInformationsDTO {
    
    @JsonView({Views.Basic.class, Views.WithEmail.class})
    private String firstName;
    
    @JsonView({Views.Basic.class, Views.WithEmail.class})
    private String lastName;

    @JsonView({Views.Address.class, Views.WithEmail.class})
    private String address;

    @JsonView({Views.Basic.class, Views.Detailed.class})
    private String phone;

    @JsonView({Views.Detailed.class, Views.WithEmail.class})
    private int age;
    
    @JsonView(Views.WithEmail.class) 
    private String email;
    
    @JsonView({Views.Detailed.class, Views.WithEmail.class})
    private List<String> medications;

    @JsonView({Views.Detailed.class, Views.WithEmail.class})
    private List<String> allergies;

    // Even though Lombok generates a final field, a collection can be modified by the external reference => making the copy in the constructor is the recommended approach for immutable DTOs.
    public PersonInformationsDTO(String firstName, String lastName, String address, String phone, int age, String email,  List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.age = age;
        this.email = email;  
        this.medications = medications != null ? List.copyOf(medications) : List.of(); // Defensive copy.
        this.allergies = allergies != null ? List.copyOf(allergies) : List.of(); // Defensive copy.
    }    

}
