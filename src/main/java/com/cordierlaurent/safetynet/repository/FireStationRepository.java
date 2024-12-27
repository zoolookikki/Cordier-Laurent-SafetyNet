package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

//import lombok.NonNull;

import com.cordierlaurent.safetynet.model.FireStation;

import lombok.NonNull;

@Repository
public class FireStationRepository {
    
    private List<FireStation> fireStations = new ArrayList<>();

    // pour récupérer la liste de toutes les stations.
    public List<FireStation> getFireStations() {
        return new ArrayList<>(fireStations);  
    }

    // pour remplacer la liste de toutes les stations.
    public void setFireStations(List<FireStation> newFireStations) {
        fireStations = new ArrayList<>(newFireStations);  
    }
    
    // ajouter une station
    public void addFireStation(@NonNull FireStation fireStation) {
        fireStations.add(fireStation);
    }

    // mise à jour d'une station. La seule clef qui est unique est l'adresse car il peut y avoir des même n° de station avec des adresses différentes.
    public boolean updateFireStationByAdress (String adress, @NonNull FireStation fireStationToUpdate) {
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equalsIgnoreCase(adress)) {
                fireStations.set(fireStations.indexOf(fireStation), fireStationToUpdate);
                return true;
            }
        }
        return false;
    }

    // Suppression idem par clef unique avec lambda.
    public boolean deleteFireStationByAdress(String adress) {
        return fireStations.removeIf(fireStation ->
            fireStation.getAddress().equalsIgnoreCase(adress)
        );
    }
    
}
