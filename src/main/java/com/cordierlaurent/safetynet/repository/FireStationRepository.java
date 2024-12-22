package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

//import lombok.NonNull;

import com.cordierlaurent.safetynet.model.FireStation;

@Repository
public class FireStationRepository {
    
    private List<FireStation> fireStations = new ArrayList<>();

    public List<FireStation> getFireStations() {
        return new ArrayList<>(fireStations);  
    }

    public void setFireStations(List<FireStation> newFireStations) {
        fireStations = new ArrayList<>(newFireStations);  
    }
    
}
