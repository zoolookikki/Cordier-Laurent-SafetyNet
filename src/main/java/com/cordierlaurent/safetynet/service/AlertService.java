package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodHouseoldDTO;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AlertService {
    
    @Autowired
    PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    public List<ChildAlertDTO> findChilddByAddress(String address) {
        log.debug("START findChilddByAddres");
        Objects.requireNonNull(address, "address cannot be null");

        List<ChildAlertDTO> childAlertDTOs = new ArrayList<>();
        
        // Rechercher la liste des personnes habitant à cette adresse.
        List<Person> persons = personRepository.findByAddress(address);
        log.debug("findByAddressr=>address="+address+",persons.size="+persons.size());

        if (!persons.isEmpty()) {
        
            for (Person person : persons) {
                int age = medicalRecordService.age(person);
                log.debug("firstName="+person.getFirstName()+",lastName="+person.getLastName()+",age="+age);
//              Si moins de 18 ans 
                if (age >=0 && age <=18) {
                    List<PersonInformationsDTO> householdMembers = new ArrayList<>();
                    // Pour chaque personne correspondant habitant à cette adresse
                    for (Person personHouseHoldMember : persons) {
                        // Attention, exclure la personne en cours de traitement.
                        if (!personHouseHoldMember.getFirstName().equalsIgnoreCase(person.getFirstName()) ||
                            !personHouseHoldMember.getLastName().equalsIgnoreCase(person.getLastName())) {
                            // Complêter la DTO avec la liste des membres du foyer (ayant donc la même adresse). 
                            householdMembers.add(new PersonInformationsDTO(
                                    personHouseHoldMember.getFirstName(),
                                    personHouseHoldMember.getLastName(),
                                    personHouseHoldMember.getAddress(),
                                    personHouseHoldMember.getPhone(),
                                    0,
                                    personHouseHoldMember.getEmail(),
                                    null,
                                    null
                                    )
                            );
                        }
                    }
                    // Ajouter dans la nouvelle DTO (prénom,nom,age).
                    childAlertDTOs.add(new ChildAlertDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            age,
                            householdMembers));
                }
            }
        }
        log.debug("END findChilddByAddres");
        return childAlertDTOs;
    }
    
    public List<String> findPhoneNumbersdByFireStation(int fireStation){
        log.debug("START findPhoneNumbersdByFireStation");
        if (fireStation <= 0) {
            throw new IllegalArgumentException("fireStation must be greater than 0");
        }

        // liste de numéros de téléphone à retourner.
        // attention aux doublons de numéro de téléphone => liste de type Set.
        // la liste doit être triée => TreeSet.
        Set<String> phoneNumbers = new TreeSet<>();

        // recherche les adresses par station => liste d'adresses.
        Set<String> addresses = fireStationRepository.findAddressesByStationNumber(fireStation);
        log.debug("findAddressesByStationNumber=>fireStation="+fireStation+",addresses.size="+addresses.size()+",addresses="+addresses);
        // recherche personnes par adresse => liste de personnes.
        List<Person> persons = personRepository.findByAddresses(addresses);
        log.debug("findByAddresses=>persons.size="+persons.size());
        
        // dans cette liste de personnes il ne faut avoir que le téléphone.
        for (Person person : persons) {
            phoneNumbers.add(person.getPhone()); 
        }
        log.debug("after duplicate filter=>phoneNumbers.size="+phoneNumbers.size());
        
        log.debug("END findPhoneNumbersdByFireStation");
        // Jackson, ne connait pour la désérialisation que : List, Map, ou Array.
        // conversion en List => création d'une nouvelle ArrayList contenant tous les éléments du Set.
        return new ArrayList<>(phoneNumbers); 
    }
    
    // List<Integer> et non List<int> car List ne peut stocker que des objets (pas des primitifs).
    public List<FloodAlertDTO> findFloodByStations(List<Integer> stations){
        log.debug("START findFloodByStations");
        Objects.requireNonNull(stations, "stations cannot be null");
        if (stations.isEmpty()) {
            throw new IllegalArgumentException("list stations cannot be null or empty");
        }
        // création de la DTO à retourner.
        List<FloodAlertDTO> floodAlertDTOs = new ArrayList<>();

        log.debug("treatment :");
        // pour chaque station
        for (int station : stations) {
            log.debug("for each station=>"+station);
            // recherche les adresses par station => liste d'adresses.
            Set<String> addresses = fireStationRepository.findAddressesByStationNumber(station);
            // création de la DTO intermédiaire
            List<FloodHouseoldDTO> floodHouseoldDTOs = new ArrayList<>();
            // pour chaque adresses de la liste d'adresse filtrées.
            for (String address : addresses) {
                log.debug("  for each address=>"+address);
                // recherche personnes par adresse => liste de personnes.
                List<Person> persons = personRepository.findByAddress(address);                
                // DTO de base
                List<PersonInformationsDTO> personInformationsDTOs = medicalRecordService.getPersonInformationsDTOs(persons);
                // si la DTO de base n'est pas vide, ajouter à la DTO intermédiaire.
                if (!personInformationsDTOs.isEmpty()) {
                    floodHouseoldDTOs.add(new FloodHouseoldDTO(address, personInformationsDTOs));
                }
            }
            // si la DTO intermédiaire n'est pas vide, ajouter à la DTO finale.
            if (!floodHouseoldDTOs.isEmpty()) {
                floodAlertDTOs.add(new FloodAlertDTO(station,floodHouseoldDTOs));
            }
        }
        log.debug("END  findFloodByStations");
        return floodAlertDTOs;
    }

}

