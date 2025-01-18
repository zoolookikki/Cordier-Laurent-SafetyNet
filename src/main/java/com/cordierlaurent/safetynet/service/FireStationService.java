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

@Service
@Log4j2
public class FireStationService extends CrudService<FireStation> {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private MedicalRecordService medicalRecordService;

    @Override
    protected boolean isSameModel(FireStation model, FireStation modelToVerify) {
        return (model.getAddress().equalsIgnoreCase(modelToVerify.getAddress()));
    }

    @Override
    protected CrudRepository<FireStation> getRepository() {
        return fireStationRepository;
    }

    public PersonsCoveredByFireStationDTO findPersonsCoveredByFireStation(int stationNumber) {
        log.debug("START findPersonsCoveredByFireStation");

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

            // pour chaque personne trouvée, on calcule d'abord son age, si il est >=0 donc valide,  on met à jour la DTO pour n'avoir que le prénom + nom + adresse + téléphone.
            for (Person person : persons) {
                int age = medicalRecordService.age(person);
                log.debug("firstName="+person.getFirstName()+",lastName="+person.getLastName()+",age="+age);
                // on définit sa catégorie. 
                if (age >=0) {
                    if (age > 18) {
                        numberOfAdults++;
                    } else {
                        numberOfChildren++;
                    }

                    // on met à jour la DTO.
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
    
    public FireDTO findFireByaddress(String address) {
        log.debug("START findFireByaddress");
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
            // DTO de base
            personInformationsDTOs = medicalRecordService.getPersonInformationsDTOs(persons);
        }
        log.debug("END findFireByaddress");
        //Créer la DTO finale dans laquelle on met la station + le contenu de la DTO de base.
        return new FireDTO (station, personInformationsDTOs);
    }
    
}


