package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.FireStation;

@Repository
public class FireStationRepository extends CrudRepository<FireStation> {
    
    @Override
    public boolean containsId(String[] id, FireStation fireStation) {
        // id invalide => clef unique = adresse
        return ((id.length == 1) && fireStation.getAddress().equalsIgnoreCase(id[0]));
    }
    
    // c'est le mode par défaut.
    public boolean deleteByAddress(String address) {
        return deleteModelByUniqueKey(new String[]{address});
    }
    
    /*
    la méthode removeIf parcourt chaque élément de la liste et supprime les éléments qui satisfont une condition (prédicat).
    le prédicat est une expression lambda : model -> model.getStation() == station => est ce que le model donné par removeIf est égal à la station.
    removeIf renvoit true si au moins un élément a été supprimé.
     */
    public boolean deleteByStation(int station) {
        return models.removeIf(model -> model.getStation() == station);
    }
    
    public List<String> findAddressesByStationNumber(int stationNumber){
        List<String> addresses = new ArrayList<>();
        for (FireStation fireStation : this.getModels()) {
            if (fireStation.getStation() == stationNumber) {
                addresses.add(fireStation.getAddress());
            }
        }
        return addresses;
    }
    
    public int findStationByAddress(String address) {
        for (FireStation fireStation : this.getModels()) {
            if (fireStation.getAddress().equalsIgnoreCase(address)) {
                return fireStation.getStation();
            }
        }
        // aucune station trouvée.
        return 0;
    }
    
}

