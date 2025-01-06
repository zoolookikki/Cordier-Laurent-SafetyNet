package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodHouseoldDTO;
import com.cordierlaurent.safetynet.dto.PersonBasicInformationsDTO;
import com.cordierlaurent.safetynet.dto.PersonHealthInformationsDTO;
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
        List<ChildAlertDTO> childAlertDTOs = new ArrayList<>();
  
        // Rechercher la liste des personnes habitant à cette adresse.
        List<Person> persons = personRepository.findByAddress(address);
        log.debug("findChilddByAddress/findByAddressr=>address="+address+",perons.size="+persons.size());

        if (!persons.isEmpty()) {
        
            for (Person person : persons) {
                int age = medicalRecordService.age(person);
                log.debug("findChilddByAddress/firstName="+person.getFirstName()+",lastName="+person.getLastName()+",age="+age);
//              Si moins de 18 ans 
                if (age >=0 && age <=18) {
                    List<PersonBasicInformationsDTO> householdMembers = new ArrayList<>();
                    // Pour chaque personne correspondant habitant à cette adresse
                    for (Person personHouseHoldMember : persons) {
                        // Attention, exclure la personne en cours de traitement.
                        if (!personHouseHoldMember.getFirstName().equalsIgnoreCase(person.getFirstName()) ||
                            !personHouseHoldMember.getLastName().equalsIgnoreCase(person.getLastName())) {
                            // Complêter la DTO avec la liste des membres du foyer (ayant donc la même adresse). 
                            householdMembers.add(new PersonBasicInformationsDTO(
                                    personHouseHoldMember.getFirstName(),
                                    personHouseHoldMember.getLastName(),
                                    personHouseHoldMember.getAddress(),
                                    personHouseHoldMember.getPhone()
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
        return childAlertDTOs;
    }
    
    public List<String> findPhoneNumbersdByFireStation(int fireStation){
        // liste de numéros de téléphone à retourner.
        // attention aux doublons de numéro de téléphone => liste de type Set.
        // la liste doit être triée => TreeSet.
        Set<String> phoneNumbers = new TreeSet<>();

        // recherche les adresses par station => liste d'adresses.
        List<String> addresses = fireStationRepository.findAddressesByStationNumber(fireStation);
        // recherche personnes par adresse => liste de personnes.
        List<Person> persons = personRepository.findByAddresses(addresses);
        
        // dans cette liste de personnes il ne faut avoir que le téléphone.
        for (Person person : persons) {
            phoneNumbers.add(person.getPhone()); 
        }
        
        // Jackson, ne connait pour la désérialisation que : List, Map, ou Array.
        // conversion en List => création d'une nouvelle ArrayList contenant tous les éléments du Set.
        return new ArrayList<>(phoneNumbers); 
    }
    
    // List<Integer> et non List<int> car List ne peut stocker que des objets (pas des primitifs).
    public List<FloodAlertDTO> findFloodByStations(List<Integer> stations){
        log.debug("START findFloodByStations");

        // création de la DTO à retourner.
        List<FloodAlertDTO> floodAlertDTOs = new ArrayList<>();

        log.debug("treatment :");
        // pour chaque station
        for (int station : stations) {
            log.debug("for each station=>"+station);
            // recherche les adresses par station => liste d'adresses.
            List<String> addresses = fireStationRepository.findAddressesByStationNumber(station);
            // création de la DTO intermédiaire
            List<FloodHouseoldDTO> floodHouseoldDTOs = new ArrayList<>();
            // pour chaque adresses de la liste d'adresse filtrées.
            for (String address : addresses) {
                log.debug("  for each address=>"+address);
                // recherche personnes par adresse => liste de personnes.
                List<Person> persons = personRepository.findByAddress(address);                
                // DTO de base
                List<PersonHealthInformationsDTO> personHealthInformationsDTOs = medicalRecordService.getPersonHealthInformationsDTOs(persons);
                // si la DTO de base n'est pas vide, ajouter à la DTO intermédiaire.
                if (!personHealthInformationsDTOs.isEmpty()) {
                    floodHouseoldDTOs.add(new FloodHouseoldDTO(address, personHealthInformationsDTOs));
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

