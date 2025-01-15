package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodAlertDTO;
import com.cordierlaurent.safetynet.dto.PersonHealthInformationsDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.FireStationRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@SpringBootTest
public class AlertServiceTest {

    @MockitoBean
    private PersonRepository personRepository;

    @MockitoBean
    private FireStationRepository fireStationRepository;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private AlertService alertService;

    private static Person person1;
    private static Person person3;
    private static FireStation fireStation1;
    private static MedicalRecord medicalRecordPerson1;
    private static MedicalRecord medicalRecordPerson3;

    @BeforeAll
    static void setUp() {
        person1 = EntityDataTest.createPerson1();
        person3 = EntityDataTest.createPerson3();
        fireStation1 = EntityDataTest.createFireStation1();
        medicalRecordPerson1 = EntityDataTest.createMedicalRecord1();
        medicalRecordPerson3 = EntityDataTest.createMedicalRecord3();
    }
    
    
    @Test
    @DisplayName("Find children by address : success")
    void findChilddByAddressTest() {

        // given
        when(personRepository.findByAddress(EntityDataTest.getPerson1And3AddressIgnoreCaseMatch()))
            .thenReturn(List.of(person1, person3));
        // child : do not put a date of birth calculation here otherwise the test would no longer be valid in the future.
        when(medicalRecordService.age(person1))
            .thenReturn(1);
        // adult.
        when(medicalRecordService.age(person3))
            .thenReturn(19);

        // when
        List<ChildAlertDTO> result = alertService.findChilddByAddress(EntityDataTest.getPerson1And3AddressIgnoreCaseMatch());

        // then
        assertThat(result).hasSize(1);
    }
    
    @Test
    @DisplayName("Find children by address : fail")
    void findChilddByAddressFailTest() {

        // given
        when(personRepository.findByAddress(EntityDataTest.getPerson1And3AddressIgnoreCaseMatch()))
            .thenReturn(List.of(person1, person3));
        when(medicalRecordService.age(any()))
            .thenReturn(19);

        // when
        List<ChildAlertDTO> result = alertService.findChilddByAddress(EntityDataTest.getPerson1And3AddressIgnoreCaseMatch());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Find phone numbers by fire station ; success")
    void findPhoneNumbersdByFireStationTest() {

        // given
        when(fireStationRepository.findAddressesByStationNumber(fireStation1.getStation()))
            .thenReturn(Set.of(fireStation1.getAddress()));
        when(personRepository.findByAddresses(Set.of(fireStation1.getAddress())))
            .thenReturn(List.of(person1, person3));

        // when
        List<String> result = alertService.findPhoneNumbersdByFireStation(fireStation1.getStation());

        // then
        // only one because the 2 people have the same, there is therefore a matching of duplicates.
        assertThat(result).hasSize(1);
        assertThat(result).containsExactlyInAnyOrder(person1.getPhone()); // or person3
    }

    @Test
    @DisplayName("Find phone numbers by fire station : fail")
    void findPhoneNumbersdByFireStationFailTest() {

        // given
        when(fireStationRepository.findAddressesByStationNumber(EntityDataTest.getStationNotExist()))
                .thenReturn(Collections.emptySet());

        // when
        List<String> result = alertService.findPhoneNumbersdByFireStation(EntityDataTest.getStationNotExist());

        // then
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("Find flood by stations : success")
    void findFloodByStationsTest() {

        // given
        when(fireStationRepository.findAddressesByStationNumber(fireStation1.getStation()))
            .thenReturn(Set.of(fireStation1.getAddress()));
        when(personRepository.findByAddress(fireStation1.getAddress()))
            .thenReturn(List.of(person1, person3));
        when(medicalRecordService.getPersonHealthInformationsDTOs(List.of(person1, person3)))
        .thenReturn(List.of(
                new PersonHealthInformationsDTO(
                           person1.getFirstName(),
                           person1.getLastName(), 
                           person1.getPhone(), 
                           19, 
                           medicalRecordPerson1.getMedications(), 
                           medicalRecordPerson1.getAllergies()),
                new PersonHealthInformationsDTO(
                        person3.getFirstName(),
                        person3.getLastName(), 
                        person3.getPhone(), 
                        1, 
                        medicalRecordPerson3.getMedications(), 
                        medicalRecordPerson3.getAllergies())
                ));

        
        // when
        List<FloodAlertDTO> result = alertService.findFloodByStations(List.of(fireStation1.getStation()));

        // then
        // only one firestation requested.
        assertThat(result).hasSize(1);
        // person1 and person3 are members of the same family.
        assertThat(result.get(0).getHouseholds().get(0).getPersons()).hasSize(2);
    }

    @Test
    @DisplayName("Find flood by stations : fail")
    void findFloodByStationsFailTest() {
        // given
        when(fireStationRepository.findAddressesByStationNumber(EntityDataTest.getStationNotExist()))
            .thenReturn(Collections.emptySet());

        // when
        List<FloodAlertDTO> result = alertService.findFloodByStations(List.of(EntityDataTest.getStationNotExist()));

        // then
        assertThat(result).isEmpty();
    }
    
}

