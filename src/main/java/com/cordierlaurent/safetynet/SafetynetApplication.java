package com.cordierlaurent.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.MessageService;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2 // plus besoin de faire pour chaque classe : private static final Logger logger = LogManager.getLogger(SafetynetApplication.class);
public class SafetynetApplication implements CommandLineRunner {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JsonDataRepository jsonDataRepository;
    
    public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
    }

    // mémo : après démarrage complet de spring (même si Logger n'en a pas besoin).
    @Override
	public void run(String... args) throws Exception {
	    log.info(messageService.getMessage("log.info"));
	    log.debug(messageService.getMessage("log.debug"));
	    log.error(messageService.getMessage("log.error"));
	    jsonDataRepository.load();
	}

}

