package com.cordierlaurent.safetynet.model;

import lombok.Data;

import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;

@Data 
@AllArgsConstructor 
public class Person {
    
    @NotBlank(message = "First name is required")    
    private String firstName;

    @NotBlank(message = "Name is required")    
    private String lastName;

    @NotBlank(message = "Address is required")    
    private String address;

    @NotBlank(message = "The city is required")    
    private String city;

    @NotBlank(message = "Zip code is required")    
    @Pattern(regexp = "\\d{5}", message = "The zip code must be 5 digits")
    private String zip; 

    @NotBlank(message = "Telephone number is required")    
//    @Pattern(regexp = "\\d{10}", message = "The phone number must be 10 digits")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "The phone number should be in the format 123-456-7890")
    private String phone;

    @NotBlank(message = "Email is required")    
    @Email(message = "Email must be valid")
    private String email;
    
}
