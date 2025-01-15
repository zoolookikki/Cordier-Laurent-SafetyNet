package com.cordierlaurent.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

//@Value mieux que @Data pour les DTO car cela génère tous les champs en final, ce qui signifie qu'ils ne peuvent pas être modifiés après l'initialisation.
//pour éviter pbs de modification des DTO car passage par référence en argument et en retour de fonction.
@Value
@AllArgsConstructor
public class PersonBasicInformationsDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // pour un test.
    //private String birthdate;
}
