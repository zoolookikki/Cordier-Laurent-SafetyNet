package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.dto.FireDTO;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service class for managing fire stations and retrieving related data.
 * Provides CRUD operations.
 */
@Service
@Log4j2
public class FireStationService extends CrudService<FireStation> {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private MedicalRecordService medicalRecordService;

    /**
     * Compares two fire station models to determine if they are the same.
     * A fire station model is considered the same if it has the same address name and station number.
     *
     * @param model the existing fire station model.
     * @param modelToVerify the fire station model to verify.
     * @return true if the models have the same address and station number, false otherwise.
     */
    @Override
    protected boolean isSameModel(FireStation model, FireStation modelToVerify) {
        return (model.getAddress().equalsIgnoreCase(modelToVerify.getAddress()) &&
               model.getStation() == modelToVerify.getStation());
    }

    /**
     * Provides the repository used for CRUD operations on fire stations.
     *
     * @return the repository instance managing fire stations.
     */
    @Override
    protected CrudRepository<FireStation> getRepository() {
        return fireStationRepository;
    }

    /**
     * Retrieves a list of people covered by a fire station along with their details and statistics.
     *
     * @param stationNumber the fire station number.
     * @return a DTO containing the list of people covered and statistics about adults and children.
     * @throws IllegalArgumentException if stationNumber is less than or equal to 0.
     */
    /*
    recherche firestations par station => liste d'adresses.
    recherche personnes par adresse => liste de personnes.
    dans cette liste de personnes il ne faut avoir que le prénom + nom + adresse + téléphone.
    calculer le nombre de personnes > et <=18. 
    */
    public PersonsCoveredByFireStationDTO findPersonsCoveredByFireStation(int stationNumber) {
        log.debug("START findPersonsCoveredByFireStation");
        if (stationNumber <= 0) {
            throw new IllegalArgumentException("stationNumber must be greater than 0");
        }        

        List<PersonInformationsDTO> personInformationsDTO = new ArrayList<>();
        int numberOfAdults = 0;
        int numberOfChildren = 0;    
        
        // recherche firestations par station => liste d'adresses.
        Set<String> addresses = fireStationRepository.findAddressesByStationNumber(stationNumber);
        log.debug("findAddressesByStationNumber=>stationNumber="+stationNumber+",addresses.size="+addresses.size()+",adresses="+addresses);

        // recherche personnes par adresse => liste de personnes.
        List<Person> persons = personRepository.findByAddresses(addresses);
        log.debug("findByAddresses=>persons.size="+persons.size());
        
        if (!persons.isEmpty()) {

            for (Person person : persons) {
                int age = medicalRecordService.age(person);
                log.debug("firstName="+person.getFirstName()+",lastName="+person.getLastName()+",age="+age);
                if (age >=0) {
                    if (age > 18) {
                        numberOfAdults++;
                    } else {
                        numberOfChildren++;
                    }

                    personInformationsDTO.add(
                            new PersonInformationsDTO(
                                    person.getFirstName(), 
                                    person.getLastName(), 
                                    person.getAddress(), 
                                    person.getPhone(),
                                    0,
                                    person.getEmail(), 
                                    null,
                                    null
                                    )
                            );
                }
            }
        }
        log.debug("END findPersonsCoveredByFireStation");
        return new PersonsCoveredByFireStationDTO(personInformationsDTO, numberOfAdults, numberOfChildren);
    }
    
    /**
     * Retrieves information about people living at a specific address and the fire station covering that address.
     *
     * @param address the address to search for.
     * @return a DTO containing the fire station number and the list of people living at the address.
     * @throws IllegalArgumentException if address is null or blank.
     */
    /*
    Rechercher la station correspondante à cette adresse.
    Si elle existe, rechercher la liste des personnes habitant à cette adresse.
    créer une nouvelle DTO contenant prénom+nom+téléphone+age+antécédents médicaux.
    Pour chaque personne 
        Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
        Calculer l'age 
        Ajouter à la DTO.  
    Créer une nouvelle DTO2 dans laquelle on met la station + le contenu de la DTO.
    */
    public FireDTO findFireByaddress(String address) {
        log.debug("START findFireByaddress");
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("address cannot be null or blank");
        }        
        
        // créer la DTO de base qui contiendra prénom+nom+téléphone+age+antécédents médicaux.
        List<PersonInformationsDTO> personInformationsDTOs = new ArrayList<>();

        // Rechercher la station correspondante à cette adresse.
        int station = fireStationRepository.findStationByAddress(address);
        log.debug("fireStationRepository.findStationByAddress=>address="+address+",station="+station);
        // Si la station existe
        if (station > 0) {
            // rechercher la liste des personnes habitant à cette adresse.
            List<Person> persons = personRepository.findByAddress(address);
            log.debug("personRepository.findByAddress=>address="+address+",persons.size="+persons.size());
            /*
            Pour chaque personne 
            Trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
            Calculer l'age 
            Ajouter à la DTO de base
            */ 
            personInformationsDTOs = medicalRecordService.getPersonInformationsDTOs(persons);
        }
        log.debug("END findFireByaddress");
        //Créer la DTO finale dans laquelle on met la station + le contenu de la DTO de base.
        return new FireDTO (station, personInformationsDTOs);
    }
    
}


