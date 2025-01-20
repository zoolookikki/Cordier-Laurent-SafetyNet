package com.cordierlaurent.safetynet.repository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.FireStation;

import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
public class FireStationRepository extends CrudRepository<FireStation> {
    
    @Override
    public boolean containsId(String[] id, FireStation fireStation) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(fireStation, "fireStation cannot be null");
        // id invalide => clef unique = adresse + numéro de station.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be address + station");
        }
        return  fireStation.getAddress().equalsIgnoreCase(id[0]) &&
                String.valueOf(fireStation.getStation()).equals(id[1]);
        
    }
    
    public Set<String> findAddressesByStationNumber(int stationNumber){
        if (stationNumber <= 0) {
            throw new IllegalArgumentException("Station number must be greater than 0");
        }
        // pour filtrer les doublons d'adresse.
        Set<String> addresses = new HashSet<>();        
        for (FireStation fireStation : this.getModels()) {
            if (fireStation.getStation() == stationNumber) {
                if (!addresses.add(fireStation.getAddress())) {
                    log.debug("findAdressesByStationNumber,filtering="+fireStation);
                }
            }
        }
        return addresses;
    }
    
    public int findStationByAddress(String address) {
        Objects.requireNonNull(address, "address cannot be null");
        if (address.isBlank()) { 
            throw new IllegalArgumentException("Address cannot be empty, or blank");
        }        
        for (FireStation fireStation : this.getModels()) {
            if (fireStation.getAddress().equalsIgnoreCase(address)) {
                return fireStation.getStation();
            }
        }
        // aucune station trouvée.
        return 0;
    }
    
}

