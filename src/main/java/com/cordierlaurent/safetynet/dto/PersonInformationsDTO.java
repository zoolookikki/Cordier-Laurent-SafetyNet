package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

//@Value mieux que @Data pour les DTO car cela génère tous les champs en final, ce qui signifie qu'ils ne peuvent pas être modifiés après l'initialisation.
//pour éviter pbs de modification des DTO car passage par référence en argument et en retour de fonction.
@Value
// Vue par défaut, ne montre que "Basic".
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

    // Même si Lombok génère un champ final, une collection peut être modifiée par la référence externe => faire la copie dans le constructeur est l'approche recommandée pour des DTO immuables.
    public PersonInformationsDTO(String firstName, String lastName, String address, String phone, int age, String email,  List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.age = age;
        this.email = email;  
        this.medications = medications != null ? List.copyOf(medications) : List.of(); // Copie défensive
        this.allergies = allergies != null ? List.copyOf(allergies) : List.of(); // Copie défensive
    }    

}
