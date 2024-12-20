package com.cordierlaurent.safetynet.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor //  Génère un constructeur avec tous les arguments.
@SuppressWarnings("unused") // pour Eclipse.
public class FireStation {
    private String address;
    private int station;
}
