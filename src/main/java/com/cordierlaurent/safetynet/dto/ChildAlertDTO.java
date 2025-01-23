package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

/**
 * Data Transfer Object representing a child and their household members for alert purposes.
 *
 */
/*
@Value better than @Data for DTOs because it generates all fields final, meaning they cannot be changed after initialization.
To avoid problems with modifying DTOs due to passing by reference in argument and function return.
*/
@Value
/*
Same as @JsonProperty to change the name, but for the class.
Doesn't work without WRAP_ROOT_VALUE (which is not enabled by default) => change the default configuration => quite heavy modification => not worth it here.
*/
//@JsonRootName("children") 
@JsonView(Views.Basic.class)
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<PersonInformationsDTO> householdMembers;
    
    // Even though Lombok generates a final field, a collection can be modified by the external reference => making the copy in the constructor is the recommended approach for immutable DTOs.
    public ChildAlertDTO(String firstName, String lastName, int age, List<PersonInformationsDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = List.copyOf(householdMembers); // Defensive copy.
    }    
}


