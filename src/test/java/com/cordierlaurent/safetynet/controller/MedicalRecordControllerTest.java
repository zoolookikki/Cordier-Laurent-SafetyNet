package com.cordierlaurent.safetynet.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.MedicalRecordService;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest  extends CrudControllerTest<MedicalRecord> {

    @MockitoBean // replaces deprecated @MockBean
    private MedicalRecordService medicalRecordService;

    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    @Override
    protected String getBaseUrl() {
        return "/medicalrecord";
    }

    @Override
    protected MedicalRecord getValidModel() {
        return new MedicalRecord("John", "Doe", "01/01/1990", List.of("medication1", "medication2"), List.of("allergy1"));
    }

    @Override
    protected MedicalRecord getUpdatedModel() {
        return new MedicalRecord("John", "Doe", "01/01/1991", List.of("updatedMedication"), List.of("updatedAllergy"));
    }

    @Override
    protected CrudService<MedicalRecord> getMockService() {
        return medicalRecordService;
    }

    @Override
    protected String[] getIdForUpdate() {
        return new String[]{"John", "Doe"};
    }    
}
