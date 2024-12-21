package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
@NoArgsConstructor // Jackson a besoin d'un constructeur par défaut (sans argument) pour instancier l'objet lors de la désérialisation.
public class FireStation {
    private String address;
    private int station;
}
