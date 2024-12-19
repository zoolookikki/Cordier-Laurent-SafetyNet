package com.cordierlaurent.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class SafetynetApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(SafetynetApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
    }

    // mémo : après démarrage complet de spring (même si Logger n'en a pas besoin).
    @Override
	public void run(String... args) throws Exception {
	    logger.info("test display info");
	    logger.debug("test display debug");
	    logger.error("test display error");
	}

}

