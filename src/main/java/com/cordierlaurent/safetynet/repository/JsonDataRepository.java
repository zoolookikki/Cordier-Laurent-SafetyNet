package com.cordierlaurent.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.model.EntityContainer;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
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
            /*
            contrôle des fichiers json :
                - doublons => clef unique pour Person et MedicalRecord (prénom+nom), pour FireStations (address)
                    ==> Utilisation d'une Map pour stocker filtrer les doublons par clef unique.
            */
            if (entityContainer.getPersons() != null) {
                Map<String, Person> personsByUniqueKey = new HashMap<>();
                for (Person person : entityContainer.getPersons()) {
                    String uniqueKey = person.getFirstName() + person.getLastName();
                    if (personsByUniqueKey.putIfAbsent(uniqueKey, person) != null) {
                        log.warn("JsonDataRepository/duplicate person filtering : "+person);
                    }
                }
                personRepository.setModels(new ArrayList<>(personsByUniqueKey.values()));
                log.debug("JsonDataRepository : list person ok");
            } else {
                log.debug("JsonDataRepository : list person null");                
            }
            if (entityContainer.getFireStations() != null) {
                Map<String, FireStation> fireStationsByUniqueKey = new HashMap<>();
                for (FireStation fireStation : entityContainer.getFireStations()) {
                    String uniqueKey = fireStation.getAddress();
                    if (fireStationsByUniqueKey.putIfAbsent(uniqueKey, fireStation) != null) {
                        log.warn("JsonDataRepository/duplicate fireStation filtering : "+fireStation);
                    }
                }
                fireStationRepository.setModels(new ArrayList<>(fireStationsByUniqueKey.values()));
                log.debug("JsonDataRepository : list firestation ok");                
            } else {
                log.debug("JsonDataRepository : list firestation null");                
            }
            if (entityContainer.getMedicalRecords() != null) {
                Map<String, MedicalRecord> medicalRecordsByUniqueKey = new HashMap<>();
                for (MedicalRecord medicalRecord : entityContainer.getMedicalRecords()) {
                    String uniqueKey = medicalRecord.getFirstName() + medicalRecord.getLastName();
                    if (medicalRecordsByUniqueKey.putIfAbsent(uniqueKey, medicalRecord) != null) {
                        log.warn("JsonDataRepository/duplicate medicalRecord filtering : "+medicalRecord);
                    }
                }
                medicalRecordRepository.setModels(new ArrayList<>(medicalRecordsByUniqueKey.values()));
                log.debug("JsonDataRepository : list medicalrecord ok");                
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
