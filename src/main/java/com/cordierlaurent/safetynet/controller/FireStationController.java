package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
// la route est définie ici dans la classe fille.
@RequestMapping("/firestation")
public class FireStationController extends CrudController<String, FireStation>{

    @Autowired
    private FireStationService fireStationService;
    
    @Override
    protected boolean checkModel(FireStation model) {
        return (model.getAddress() != null && !model.getAddress().isBlank()); 
    }

    @Override
    protected CrudService<String, FireStation> getService() {
        return fireStationService;
    }
}

