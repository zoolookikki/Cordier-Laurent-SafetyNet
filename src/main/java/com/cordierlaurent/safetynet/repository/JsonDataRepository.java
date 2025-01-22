package com.cordierlaurent.safetynet.repository;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cordierlaurent.safetynet.exception.JsonFileException;
import com.cordierlaurent.safetynet.model.EntityContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Repository responsible for managing data from a JSON file.
 * 
 * <p>This class handles the initialization, loading, and saving of data  from/to a JSON file. 
 * It uses ObjectMapper for JSON deserialization and serialization.
 * 
 * <p>The repository must be initialized using the init(String) method before loading or saving data.</p>
 */
@Repository
@Data
@Log4j2
public class JsonDataRepository {
    
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private ObjectMapper objectMapper;
    
    private boolean init = false;
    private File jsonFile;
    private String jsonFileName;
    
    /**
     * Initializes the repository with a JSON file.
     *
     * @param jsonFileName The name of the JSON file.
     * @throws IllegalArgumentException if the file blank, does not exist, or is not a valid file.
     */
    // à cause du @Autowired qui m'empêche d'initialiser avec le constructeur... Il y a des solutions, à revoir.
    public void init (String jsonFileName) {
        if ((jsonFileName == null) || jsonFileName.isBlank()) {
            throw new IllegalArgumentException("jsonFileName must not be null or blank.");
        }
        this.jsonFileName = jsonFileName;

        jsonFile = new File(jsonFileName);
        if (!jsonFile.exists() || !jsonFile.isFile()) {
            throw new JsonFileException("jsonFile must exist and be a valid file : " + jsonFileName);
        }
        init = true;
        log.debug("Repository initialized with JSON file : " + jsonFileName);                
    }

    /**
     * Loads data from the JSON file into the repositories.
     *
     * @throws JsonFileException if loading fails or the data is invalid.
     */
    public void load() {
        try {
            if (!init) {
                throw new JsonFileException("Repository not initialized. Call init() first.");
            }
            EntityContainer entityContainer = objectMapper.readValue(jsonFile, EntityContainer.class);
            if (entityContainer == null) {
                throw new JsonFileException("EntityContainer is null, unable to load data.");
            }
            if ((entityContainer.getPersons() == null) ||
                    (entityContainer.getFireStations() == null) ||
                    (entityContainer.getMedicalRecords() == null)) {
                throw new JsonFileException("Bad JSON format, unable to load data.");
            }
            
            personRepository.setModels(entityContainer.getPersons());
            fireStationRepository.setModels(entityContainer.getFireStations());
            medicalRecordRepository.setModels(entityContainer.getMedicalRecords());
            
            log.debug("Data loaded successfully");
            
        } catch (IOException e) {
            throw new JsonFileException("Unable to load JSON file : " + jsonFileName);
        }
    }
    
    /**
     * Saves the current state of the repositories to the JSON file.
     * 
     * @throws JsonFileException if the repository is not initialized or if saving fails.
     */
    public void save() {
        try {
            if (!init) {
                throw new JsonFileException("Repository not initialized. Call init() first.");
            }
            EntityContainer entityContainer = new EntityContainer(
                    personRepository.getModels(),
                    fireStationRepository.getModels(),
                    medicalRecordRepository.getModels()
                    );
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, entityContainer);

            log.debug("Data saved successfully");            

        } catch (IOException e) {
            throw new JsonFileException("Unable to save JSON file: " + jsonFileName);
        }
    }
    
}
