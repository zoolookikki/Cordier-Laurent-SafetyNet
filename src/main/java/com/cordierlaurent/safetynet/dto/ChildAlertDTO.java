package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

// @Value mieux que @Data pour les DTO car cela génère tous les champs en final, ce qui signifie qu'ils ne peuvent pas être modifiés après l'initialisation.
// pour éviter pbs de modification des DTO car passage par référence en argument et en retour de fonction.
@Value
//idem @JsonProperty pour changer le nom, mais pour la classe.
// mais ne fonctionne pas sans WRAP_ROOT_VALUE (qui n'est pas activé par défaut) => changer la configuration par défault => modification assez lourde => ne vaut pas le coup ici.
//@JsonRootName("children") 
@JsonView(Views.Address.class)
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<PersonInformationsDTO> householdMembers;
    
    // Même si Lombok génère un champ final, une collection peut être modifiée par la référence externe => faire la copie dans le constructeur est l'approche recommandée pour des DTO immuables.
    public ChildAlertDTO(String firstName, String lastName, int age, List<PersonInformationsDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = List.copyOf(householdMembers); // Copie défensive
    }    
}


