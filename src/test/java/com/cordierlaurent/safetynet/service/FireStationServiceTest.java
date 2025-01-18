package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.dto.FireDTO;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@SpringBootTest
public class FireStationServiceTest {

    @MockitoBean
    private FireStationRepository fireStationRepository;

    @MockitoBean
    private PersonRepository personRepository;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FireStationService fireStationService;
    
    private static FireStation fireStation1;
    private static FireStation fireStation3;
    private static Person person1;
    private static Person person3;
    private static MedicalRecord medicalRecordPerson1;
    private static MedicalRecord medicalRecordPerson3;
    
    @BeforeAll
    private static void setUp() {
        fireStation1 = EntityDataTest.createFireStation1();
        fireStation3 = EntityDataTest.createFireStation3();
        person1 = EntityDataTest.createPerson1();
        person3 = EntityDataTest.createPerson3();
        medicalRecordPerson1 = EntityDataTest.createMedicalRecord1();
        medicalRecordPerson3 = EntityDataTest.createMedicalRecord3();
    }
    
    @Test
    @DisplayName("Find persons covered by fire station")
    void findPersonsCoveredByFireStationTest() {

        // given
        when(fireStationRepository.findAddressesByStationNumber(EntityDataTest.getStation3Exist()))
            .thenReturn(Set.of(fireStation1.getAddress(), fireStation3.getAddress()));
        when(personRepository.findByAddresses(Set.of(fireStation1.getAddress(), fireStation3.getAddress())))
            .thenReturn(List.of(person1, person3));
        // adult
        when(medicalRecordService.age(person1))
            .thenReturn(19);
        // children 
        when(medicalRecordService.age(person3))
            .thenReturn(1); 
        
        // when
        PersonsCoveredByFireStationDTO result = fireStationService.findPersonsCoveredByFireStation(EntityDataTest.getStation3Exist());

        // then
        assertThat(result.getPersonInformationsDTO()).hasSize(2);
        assertThat(result.getNumberOfAdults()).isEqualTo(1);
        assertThat(result.getNumberOfChildren()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Find fire by address")
    void findFireByAddressTest() {

        // given
        when(fireStationRepository.findStationByAddress(fireStation1.getAddress()))
            .thenReturn(fireStation1.getStation());
        when(personRepository.findByAddress(fireStation1.getAddress()))
            .thenReturn(List.of(person1, person3));
        when(medicalRecordService.getPersonInformationsDTOs(List.of(person1, person3)))
            .thenReturn(List.of(
                    new PersonInformationsDTO(
                               person1.getFirstName(),
                               person1.getLastName(), 
                               person1.getAddress(),
                               person1.getPhone(), 
                               19, 
                               person1.getEmail(), 
                               medicalRecordPerson1.getMedications(), 
                               medicalRecordPerson1.getAllergies()),
                    new PersonInformationsDTO(
                            person3.getFirstName(),
                            person3.getLastName(),
                            person3.getAddress(),
                            person3.getPhone(),
                            1, 
                            person3.getEmail(), 
                            medicalRecordPerson3.getMedications(), 
                            medicalRecordPerson3.getAllergies())
                    ));

        // when
        FireDTO result = fireStationService.findFireByaddress(fireStation1.getAddress());

        // then
        assertThat(result.getStation()).isEqualTo(fireStation1.getStation());
        assertThat(result.getPersons()).hasSize(2);
    }
    
}
