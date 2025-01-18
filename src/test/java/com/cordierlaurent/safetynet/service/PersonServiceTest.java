package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@SpringBootTest
public class PersonServiceTest {
    
    private static Person person1;
    private static Person person2;
    private static Person person3;
    private static Person person4;
    private static MedicalRecord medicalRecordPerson1;
    private static MedicalRecord medicalRecordPerson2;

    @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    private PersonRepository personRepository;

    @MockitoBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonService personService;

    @BeforeAll
    private static void setUp() {
        person1 = EntityDataTest.createPerson1();
        person2 = EntityDataTest.createPerson2();
        person3 = EntityDataTest.createPerson3();
        person4 = EntityDataTest.createPerson4();
        medicalRecordPerson1 = EntityDataTest.createMedicalRecord1();
        medicalRecordPerson2 = EntityDataTest.createMedicalRecord2();
    }

    @Test
    @DisplayName("A person is unique")
    public void isUniquePersonTest(){
        
        // given
        when(personRepository.getModels())
            .thenReturn(List.of(person1, person2));
        
        // when
        // null veut dire que l'on vérifie dans un cas de création (on n'a pas d'id).
        boolean isUnique = personService.isUnique(null,person3);
        
        // then
        assertThat(isUnique).isTrue();
    }
    
    @Test
    @DisplayName("A person is not unique")
    public void isNotUniquePersonTest(){
        
        // given
        when(personRepository.getModels())
            .thenReturn(List.of(person1, person2));
        
        // when
        // On en profite pour tester la modification (on a un id).
        boolean isUnique = personService.isUnique(new String[] {person1.getFirstName(), person1.getLastName()}, person2);
        
        // then
        assertThat(isUnique).isFalse();
    }
    
    @Test
    @DisplayName("List of residents and their confidential information that matches the last name")
    void findPersonInfoByLastNameTest() {
        
        // given.
        when(personRepository.findByLastName(person1.getLastName()))
            .thenReturn(List.of(person1, person2));
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(new String[] {person1.getFirstName(), person1.getLastName()}))
            .thenReturn(Optional.of(medicalRecordPerson1));
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(new String[] {person2.getFirstName(), person2.getLastName()}))
            .thenReturn(Optional.of(medicalRecordPerson2));

        // when.
        List<PersonInformationsDTO> result = personService.findPersonInfoByLastName(person1.getLastName());

        // then.
        // car person1 et person2 ont le même nom.
        assertThat(result).hasSize(2);
    }
    
    @Test
    @DisplayName("List of residents and their confidential information that matches the last name but medical record not found")
    void findPersonInfoByLastNameButMedicalRecordNotFoundTest() {
        
        // given
        when(personRepository.findByLastName(person1.getLastName()))
            .thenReturn(List.of(person1, person2));
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(any()))
            .thenReturn(Optional.empty());

        // when
        List<PersonInformationsDTO> result = personService.findPersonInfoByLastName(person1.getLastName());

        // then
        // no medical record => no DTO created
        assertThat(result).isEmpty(); 
    }    

    @Test
    @DisplayName("List of residents and their confidential information that matches the last name but age is invalid")
    void findPersonInfoByLastNameButAgeIsInvalidTest() {
        
        // given
        when(personRepository.findByLastName(person1.getLastName()))
            .thenReturn(List.of(person1, person2));
        when(medicalRecordRepository.findMedicalRecordByUniqueKey(any()))
            .thenReturn(Optional.of(EntityDataTest.getInvalidAgeMedicalRecord()));

        // when
        List<PersonInformationsDTO> result = personService.findPersonInfoByLastName(person1.getLastName());

        // then
        // Invalid age => no DTO created
        assertThat(result).isEmpty(); 
    }

    @Test
    @DisplayName("List of email addresses of all residents of a city")
    void findEmailsByCityTest() {
        
        // given.
        when(personRepository.findByCity(any()))
            .thenReturn(List.of(person1, person2, person3, person4));

        // when.
        Set<String> result = personService.findEmailsByCity(any());

        // then.
        assertThat(result)
            // On vérifie que les doublons d'adresse e-email sont filtrés
            .hasSize(3)
            // et que les e-mails sont triés.
            .containsExactly("drk@email.com", "jaboyd@email.com", "tenz@email.com");
    }

    @Test
    @DisplayName("List of email addresses of all residents of a city bug invalid email are ignored")
    void findEmailsByCityInvalidEmailsTest() {

        // given
        when(personRepository.findByCity(any()))
            .thenReturn(List.of(person1, person2, person3, person4, EntityDataTest.getPersonInvalidData()));

        // when.
        Set<String> result = personService.findEmailsByCity(any());

        // then
        // Only valid emails should be included
        assertThat(result)
            .hasSize(3) 
            .containsExactly("drk@email.com", "jaboyd@email.com", "tenz@email.com");
    }    
}
