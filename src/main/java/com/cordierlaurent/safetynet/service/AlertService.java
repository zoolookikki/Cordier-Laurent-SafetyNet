package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.dto.PersonBasicInformationsDTO;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;
import com.cordierlaurent.safetynet.Util.DateUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AlertService {
    
    @Autowired
    PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private FireStationRepository fireStationRepository;

    public List<ChildAlertDTO> findChilddByAddress(String address) {
        List<ChildAlertDTO> childAlertDTO = new ArrayList<>();
  
        // Rechercher la liste des personnes habitant à cette adresse.
        List<Person> persons = personRepository.findByAddress(address);
        log.debug("findChilddByAddress/findByAddressr=>address="+address+",perons.size="+persons.size());

        if (!persons.isEmpty()) {
        
            for (Person person : persons) {
                // on retrouve sa date de naissance via la clef unique dans MedicalRecord.
                String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(
                           new String[]{
                                   person.getFirstName(), 
                                   person.getLastName()});

                int age = DateUtil.CalculateAge(birthdate);
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
                    childAlertDTO.add(new ChildAlertDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            age,
                            householdMembers));
                }
            }
        }
        return childAlertDTO;
    }
    
    public List<String> findPhoneNumbersdByFireStation(int fireStation){
        // list de numéros de téléphone à retourner.
        // attention aux doublons de numéro de téléphone => liste de type Set.
        // la liste doit être triée => TreeSet.
        Set<String> phoneNumbers = new TreeSet<>();

        // recherche firestations par station => liste d'adresses.
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
    

}

