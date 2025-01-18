package com.cordierlaurent.safetynet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Value;

//@Value mieux que @Data pour les DTO car cela génère tous les champs en final, ce qui signifie qu'ils ne peuvent pas être modifiés après l'initialisation.
//pour éviter pbs de modification des DTO car passage par référence en argument et en retour de fonction.
@Value
@JsonView(Views.Detailed.class) 
public class FloodAlertDTO {
    private int station;
    private List<FloodHouseoldDTO> households;
    
    // Même si Lombok génère un champ final, une collection peut être modifiée par la référence externe => faire la copie dans le constructeur est l'approche recommandée pour des DTO immuables.
    public FloodAlertDTO(int station, List<FloodHouseoldDTO> households) {
        this.station = station;
        this.households = List.copyOf(households); // Copie défensive
    }    
}
