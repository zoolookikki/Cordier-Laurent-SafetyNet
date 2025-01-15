package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.dto.PersonHealthInformationsDTO;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;

@SpringBootTest
public class MedicalRecordServiceTest {

    @MockitoBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    private static MedicalRecord medicalRecordPerson1;
    private static MedicalRecord medicalRecordPerson2;
    private static Person person1;
    private static Person person2;

    @BeforeAll
    private static void setUp() {
        medicalRecordPerson1 = EntityDataTest.createMedicalRecord1();
        medicalRecordPerson2 = EntityDataTest.createMedicalRecord2();
        person1 = EntityDataTest.createPerson1();
        person2 = EntityDataTest.createPerson2();
    }

    @Test
    @DisplayName("Calculate age : success")
    void ageSuccessTest() {
        
        // given
        when(medicalRecordRepository.findBirthdateByUniqueKey(new String[]{person1.getFirstName(), person1.getLastName()}))
            .thenReturn(medicalRecordPerson1.getBirthdate());

        // when
        int age = medicalRecordService.age(person1);

        // then
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @DisplayName("Calculate age : fail")
    void ageFailTest() {

        // given
        when(medicalRecordRepository.findBirthdateByUniqueKey(new String[]{person1.getFirstName(), person1.getLastName()}))
            .thenReturn("??/??/????");
        
        // when
        int age = medicalRecordService.age(person1);

        // then
        assertThat(age).isEqualTo(-1);
    }

    @Test
    @DisplayName("Get person health informations")
    void getPersonHealthInformationsDTOsTest() {

        // given
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(new String[]{person1.getFirstName(), person1.getLastName()}))
            .thenReturn(Optional.of(medicalRecordPerson1));
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(new String[]{person2.getFirstName(), person2.getLastName()}))
            .thenReturn(Optional.of(medicalRecordPerson2));

        // when
        List<PersonHealthInformationsDTO> result = medicalRecordService.getPersonHealthInformationsDTOs(List.of(person1, person2));

        // then
        assertThat(result).hasSize(2);
    }

}

