package com.cordierlaurent.safetynet.repository;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.FireStation;

@Repository
public class FireStationRepository extends CrudRepository<FireStation> {
    
    @Override
    public boolean containsId(String[] id, FireStation fireStation) {
        return fireStation.getAddress().equalsIgnoreCase(id[0]);
    }
    
    public boolean deleteByAddress(String address) {
        String[] id = new String[] {address};
        return deleteModelByUniqueKey (id); // c'est le mode par défaut.
    }
    
    public boolean deleteByStation(int station) {
        return models.removeIf(model -> model.getStation() == station);
    }
    
}
