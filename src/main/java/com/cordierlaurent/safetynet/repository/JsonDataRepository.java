package com.cordierlaurent.safetynet.repository;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.EntityContainer;
import com.cordierlaurent.safetynet.service.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Repository
@Data
public class JsonDataRepository {
    
    private String jsonFileName = "src/main/resources/data/data.json";
    private static final Logger logger = LogManager.getLogger(JsonDataRepository.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private ObjectMapper objectMapper;
    
    public void load() {
        try {
            EntityContainer entityContainer = objectMapper.readValue(new File(jsonFileName), EntityContainer.class);

            personRepository.setPersons(entityContainer.getPersons());
            fireStationRepository.setFireStations(entityContainer.getFireStations());
            medicalRecordRepository.setMedicalRecords(entityContainer.getMedicalRecords());

        } catch (IOException e) {
            logger.error(messageService.getMessage("json.file_not_exist") + jsonFileName);
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            EntityContainer entityContainer = new EntityContainer(
                    personRepository.getPersons(),
                    fireStationRepository.getFireStations(),
                    medicalRecordRepository.getMedicalRecords()
                    );
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFileName), entityContainer);

        } catch (IOException e) {
            logger.error(messageService.getMessage("json.write_error") + jsonFileName);
            e.printStackTrace();
        }
    }
    
}
