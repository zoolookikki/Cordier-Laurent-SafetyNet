package com.cordierlaurent.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Main class that executes the program.
 * 
 * Implements the CommandLineRunner interface allowing code to be executed after the application context has been initialized.
 */
@SpringBootApplication
@Log4j2
public class SafetynetApplication implements CommandLineRunner {

    @Autowired
    private JsonDataRepository jsonDataRepository;
    
    /** 
     * Name of the JSON file loaded dynamically from the application properties using @Value: used here to start the application in production mode and integration test mode.
     */
    @Value("${safetynet.json-file}") 
    private String jsonFileName;
    
    /**
     * Main entry point to the application.
     * 
     * @param args command line arguments (not used here).
     */
    public static void main(String[] args) {
        SpringApplication.run(SafetynetApplication.class, args);
    }

    /**
     * Method executed after Spring Boot starts.
     * 
     * Initializes and loads data from the JSON file.
     */
    @Override
	public void run(String... args) {
        jsonDataRepository.init(jsonFileName);
        jsonDataRepository.load();
	}

}

