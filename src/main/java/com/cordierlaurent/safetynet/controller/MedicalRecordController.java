package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.MedicalRecordService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
// la route est définie ici dans la classe fille.
@RequestMapping("/medicalrecord")
public class MedicalRecordController extends CrudController<MedicalRecord>{

    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Override
    protected boolean checkModel(MedicalRecord model) {
        return (model.getFirstName() != null && !model.getFirstName().isBlank() && 
                model.getLastName() != null && !model.getLastName().isBlank());
    }

    @Override
    protected CrudService<MedicalRecord> getService() {
        return medicalRecordService;
    }

    @Override
    protected boolean checkId(String[] id) {
        if ((id == null) || (id.length != 2) || id[0].isBlank() || id[1].isBlank()) {
            return false;
        } else {
            return true;
        }
    }
}

