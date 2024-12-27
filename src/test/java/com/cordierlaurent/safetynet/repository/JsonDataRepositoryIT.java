package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;

@SpringBootTest
public class JsonDataRepositoryIT {

    private final String JSON_FILENAME_TEST = "src/main/resources/data/test.json";

    @Autowired
    JsonDataRepository jsonDataRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    @DisplayName("Integration test to verify json save and load repository")
    void saveAndLoadTest() {

        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person person3 = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        FireStation fireStation1 = new FireStation("1509 Culver St", 3);
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("350mg", "100mg"), List.of("nillacilan"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jacob", "Boyd", "03/06/1989", List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of("peanut", "shellfish", "aznol"));
        
        // given
        File file = new File(JSON_FILENAME_TEST);
        if (file.exists()) {
            file.delete();
        }
       
        jsonDataRepository.setJsonFileName(JSON_FILENAME_TEST);

        // List.of crée une liste non modifiable, pour le test c'est ok et l'écriture est meilleure que des add.
        List<Person> persons = List.of(person1, person2, person3);
        List<FireStation> fireStations = List.of(fireStation1);
        List<MedicalRecord> medicalRecords = List.of(medicalRecord1, medicalRecord2);
        personRepository.setModels(persons);
        fireStationRepository.setModels(fireStations);
        medicalRecordRepository.setModels(medicalRecords);  

        // when
        jsonDataRepository.save();
        personRepository.setModels(new ArrayList<>());
        fireStationRepository.setModels(new ArrayList<>());
        medicalRecordRepository.setModels(new ArrayList<>());
        jsonDataRepository.load();
        
        // then
        assertThat(file.exists()).isTrue();
        assertThat(personRepository.getModels())
            .isNotNull()
            .hasSize(3)
            .containsExactlyInAnyOrder(person1, person2, person3); 
        assertThat(fireStationRepository.getModels())
            .isNotNull()
            .hasSize(1)
            .containsExactlyInAnyOrder(fireStation1); 
        assertThat(medicalRecordRepository.getModels())
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(medicalRecord1, medicalRecord2); 
    }
    
}
