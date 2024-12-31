package com.cordierlaurent.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.Util.DateUtil;
import com.cordierlaurent.safetynet.dto.PersonBasicInformationsDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FireStationService extends CrudService<FireStation> {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;
    
    @Override
    protected boolean isSameModel(FireStation model, FireStation modelToVerify) {
        return (model.getAddress().equalsIgnoreCase(modelToVerify.getAddress()));
    }

    @Override
    protected CrudRepository<FireStation> getRepository() {
        return fireStationRepository;
    }
    
    public boolean deleteByAddress (String address) {
        return fireStationRepository.deleteByAddress(address);
    }

    public boolean deleteByStation (int station) {
        return fireStationRepository.deleteByStation(station);
    }

    public PersonsCoveredByFireStationDTO findPersonsCoveredByFireStation(int stationNumber) {

        List<PersonBasicInformationsDTO> personBasicInformationsDTO = new ArrayList<>();
        int numberOfAdults = 0;
        int numberOfChildren = 0;    
        
        // recherche firestations par station => liste d'adresses.
        List<String> addresses = fireStationRepository.findAddressesByStationNumber(stationNumber);
        log.debug("findPersonsCoveredByFireStation/findAddressesByStationNumber=>stationNumber="+stationNumber+",addresses.size="+addresses.size());

        // recherche personnes par adresse => liste de personnes.
        List<Person> persons = personRepository.findByAddresses(addresses);
        log.debug("findPersonsCoveredByFireStation/findByAddresses=>persons.size="+persons.size());
        
        if (!persons.isEmpty()) {

            // pour chaque personne trouvée, on calcule d'abord son age, si il est >=0 donc valide,  on met à jour la DTO pour n'avoir que le prénom + nom + adresse + téléphone.
            for (Person person : persons) {

                // on retrouve sa date de naissance.
                String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(
                           new String[]{
                                   person.getFirstName(), 
                                   person.getLastName()});
                log.debug("findPersonsCoveredByFireStation/findBirthdateByUniqueKey=>firstName="+person.getFirstName()+",lastName="+person.getLastName()+",birthdate="+birthdate);

                int age = DateUtil.CalculateAge(birthdate);
                log.debug("findPersonsCoveredByFireStation/firstName="+person.getFirstName()+",lastName="+person.getLastName()+",age="+age);
                // on définit sa catégorie. 
                if (age >=0) {
                    if (age > 18) {
                        numberOfAdults++;
                    } else {
                        numberOfChildren++;
                    }

                    // on met à jour la DTO.
                    personBasicInformationsDTO.add(
                            new PersonBasicInformationsDTO(
                                    person.getFirstName(), 
                                    person.getLastName(), 
                                    person.getAddress(), 
                                    person.getPhone()
                                    // pour un test.
                                    //,birthdate
                                    )
                            );
                }
            }
        }
        return new PersonsCoveredByFireStationDTO(personBasicInformationsDTO, numberOfAdults, numberOfChildren);
    }
    
}


