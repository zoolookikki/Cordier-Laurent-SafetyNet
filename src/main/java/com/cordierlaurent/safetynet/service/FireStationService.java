package com.cordierlaurent.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.FireStationRepository;

@Service
public class FireStationService extends CrudService<FireStation> {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Override
    protected boolean isSameModel(FireStation model, FireStation modelToVerify) {
        return (model.getAddress().equalsIgnoreCase(modelToVerify.getAddress()));
    }

    @Override
    protected CrudRepository<FireStation> getRepository() {
        return fireStationRepository;
    }
    
    public boolean deleteByAddress (String address) {
        return fireStationRepository.deleteByAddress(address);
    }

    public boolean deleteByStation (int station) {
        return fireStationRepository.deleteByStation(station);
    }


}
