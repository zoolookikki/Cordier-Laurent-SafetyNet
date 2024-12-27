package com.cordierlaurent.safetynet.repository;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.EntityContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Repository
@Data
@Log4j2
public class JsonDataRepository {
    
    private String jsonFileName = "src/main/resources/data/data.json";

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private ObjectMapper objectMapper;
    
    public void load() {
        log.debug("JsonDataRepository : load called");                
        try {
            EntityContainer entityContainer = objectMapper.readValue(new File(jsonFileName), EntityContainer.class);
            
            if (entityContainer == null) {
                log.error("EntityContainer is null, unable to load data.");
                return;
            }
            // contrôle des fichiers json à faire +++
            if (entityContainer.getPersons() != null) {
                log.debug("JsonDataRepository : list person ok");                
                personRepository.setModels(entityContainer.getPersons());
            } else {
                log.debug("JsonDataRepository : list person null");                
            }
            if (entityContainer.getFireStations() != null) {
                log.debug("JsonDataRepository : list firestation ok");                
                fireStationRepository.setModels(entityContainer.getFireStations());
            } else {
                log.debug("JsonDataRepository : list firestation null");                
            }
            if (entityContainer.getMedicalRecords() != null) {
                log.debug("JsonDataRepository : list medicalrecord ok");                
                medicalRecordRepository.setModels(entityContainer.getMedicalRecords());
            } else {
                log.debug("JsonDataRepository : list medicalrecord null");                
            }

        } catch (IOException e) {
            log.error("unable to load Json file : " + jsonFileName);
            e.printStackTrace();
        }
    }
    
    public void save() {
        log.debug("JsonDataRepository : save called");                
        try {
            EntityContainer entityContainer = new EntityContainer(
                    personRepository.getModels(),
                    fireStationRepository.getModels(),
                    medicalRecordRepository.getModels()
                    );
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFileName), entityContainer);

        } catch (IOException e) {
            log.error("write error in json file : " + jsonFileName);
            e.printStackTrace();
        }
    }
    
}
