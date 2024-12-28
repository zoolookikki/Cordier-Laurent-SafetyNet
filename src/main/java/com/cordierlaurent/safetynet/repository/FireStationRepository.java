package com.cordierlaurent.safetynet.repository;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.FireStation;

@Repository
public class FireStationRepository extends CrudRepository<FireStation> {
    
    @Override
    public boolean containsId(String[] id, FireStation fireStation) {
        return fireStation.getAddress().equalsIgnoreCase(id[0]);
    }
    
}
