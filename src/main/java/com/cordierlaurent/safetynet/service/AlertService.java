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

/**
 * Service class responsible for managing alert-related operations.
 * 
 */
@Service
@Log4j2
public class AlertService {
    
    @Autowired
    PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    /**
     * Retrieves a list of children (less than or equal to 18 years old) living at a given address, along with members of their household.
     *
     * @param address the address for which to retrieve children and household members.
     * @return a list of ChildAlertDTO objects containing information about children and their household members.
     * @throws NullPointerException if the provided address is null.
     */
    /*
    Rechercher la liste des personnes habitant à cette adresse.
    Pour chaque personne :
        Trouver l'age via la clef unique dans MedicalRecord
        Si moins de 18 ans 
            Ajouter dans la nouvelle DTO (prénom,nom,age).
            Complêter la DTO avec la liste des membres du foyer (ayant donc la même adresse). Attention à exclure la personne en cours de traitement.
    
    */
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
                // Si moins de 18 ans 
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
    
    /**
     * Retrieves a list of phone numbers of people covered by a given fire station.
     *
     * @param fireStation the number of the fire station.
     * @return a sorted list of unique phone numbers.
     * @throws IllegalArgumentException if the fire station number is less than or equal to 0.
     */
    /*
    recherche les adresses par station => liste d'adresses.
    recherche personnes par adresse => liste de personnes.
    dans cette liste de personnes il ne faut avoir que le téléphone.
    attention aux doublons de numéro de téléphone => liste de type Set.
    la liste doit être triée => TreeSet.
    Renvoyer un json sous la forme :
    */
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
        // Jackson only knows for deserialization: List, Map, or Array => conversion to List => creation of a new ArrayList containing all the elements of the Set.
        return new ArrayList<>(phoneNumbers); 
    }

    /**
     * Retrieves detailed information about households served by the given fire stations in case of flooding.
     *
     * @param stations a list of fire station numbers.
     * @return a list of FloodAlertDTO objects containing household information by station.
     * @throws NullPointerException if the stations list is null.
     * @throws IllegalArgumentException if the stations list is empty.
     */
    /*
    création de la DTO FloodAlertDTO ((station, liste de FloodHouseoldDTO)
    pour chaque station
        création de la DTO FloodHouseoldDTO (address, liste de PersonHealthInformationsDTO)
        recherche des adresses de la station => liste d'adresses.
        pour chaque adresses
            recherche personnes par adresse => liste de personnes.
            création de la DTO PersonHealthInformations
            pour chaque personne
                trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
                calculer l'age 
                ajouter à la DTO PersonHealthInformationsDTO
            si la DTO PersonHealthInformationsDTO n'est pas vide
                ajouter à la DTO FloodHouseoldDTO
        si la DTO FloodHouseoldDTO n'est pas vide
            ajouter à la DTO FloodAlertDTO 
    */
    // List<Integer> and not List<int> because List can only store objects (not primitives).
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

