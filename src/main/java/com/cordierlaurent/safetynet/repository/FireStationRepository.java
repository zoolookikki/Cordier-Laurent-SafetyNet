package com.cordierlaurent.safetynet.repository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.FireStation;

import lombok.extern.log4j.Log4j2;

/**
 * Repository class for managing FireStation entities.
 * 
 * <p>Extends CrudRepository to provide CRUD operations and specific methods related to fire stations.</p> 
 * 
 */
@Repository
@Log4j2
public class FireStationRepository extends CrudRepository<FireStation> {
    
    /**
     * Checks if a given FireStation contains a unique identifier.
     * 
     * <p>The identifier must consist of two parts:</p>
     * <ul>
     *   <li>The address of the fire station (case-insensitive).</li>
     *   <li>The station number as a string.</li>
     * </ul>
     * 
     * @param id the unique identifier as an array of strings: [address, stationNumber].
     * @param fireStation the fire station entity to compare with.
     * @return true if the fire station matches the given identifier, false otherwise.
     * @throws IllegalArgumentException if the id does not have exactly two elements.
     * @throws NullPointerException if id or fireStation is null.
     */
    @Override
    public boolean containsId(String[] id, FireStation fireStation) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(fireStation, "fireStation cannot be null");
        // invalid id => unique key = address + station number.
        if (id.length != 2) {
            throw new IllegalArgumentException("Id must be address + station");
        }
        return  fireStation.getAddress().equalsIgnoreCase(id[0]) &&
                String.valueOf(fireStation.getStation()).equals(id[1]);
        
    }
    
    /**
     * Retrieves all addresses associated with a given fire station number.
     * 
     * @param stationNumber the fire station number.
     * @return a Set of addresses covered by the station.
     * @throws IllegalArgumentException if stationNumber is less than or equal to zero.
     */
    public Set<String> findAddressesByStationNumber(int stationNumber){
        if (stationNumber <= 0) {
            throw new IllegalArgumentException("Station number must be greater than 0");
        }
        // to filter address duplicates.
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
    
    /**
     * Finds the fire station number associated with a given address.
     * 
     * @param address the address to search for.
     * @return the station number if found or 0 if no station is associated with the address.
     * @throws NullPointerException if address is null.
     * @throws IllegalArgumentException if address is empty or blank.
     */
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
        // No station found
        return 0;
    }
    
}

