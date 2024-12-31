package com.cordierlaurent.safetynet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//idem @JsonProperty pour changer le nom, mais pour la classe.
// mais ne fonctionne pas sans WRAP_ROOT_VALUE (qui n'est pas activé par défaut) => changer la configuration par défault => modification assez lourde => ne vaut pas le coup ici.
//@JsonRootName("children") 
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<PersonBasicInformationsDTO> householdMembers;
}


