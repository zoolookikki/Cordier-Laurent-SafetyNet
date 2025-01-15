package com.cordierlaurent.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2 // plus besoin de faire pour chaque classe : private static final Logger logger = LogManager.getLogger(SafetynetApplication.class);
public class SafetynetApplication implements CommandLineRunner {

    @Autowired
    private JsonDataRepository jsonDataRepository;
    
    // Charge la propriété dynamique
    @Value("${safetynet.json-file}") 
    private String jsonFileName;
    
    public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
    }

    // mémo : après démarrage complet de spring (même si Logger n'en a pas besoin).
    @Override
	public void run(String... args) throws Exception {
	    jsonDataRepository.init(jsonFileName);
	    jsonDataRepository.load();
	}

}

