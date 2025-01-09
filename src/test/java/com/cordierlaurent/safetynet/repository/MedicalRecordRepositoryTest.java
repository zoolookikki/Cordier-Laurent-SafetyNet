package com.cordierlaurent.safetynet.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.MedicalRecord;

//import com.cordierlaurent.safetynet.model.MedicalRecord;

public class MedicalRecordRepositoryTest extends CrudRepositoryTest<MedicalRecord> {

    private MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();
    
    // je ne peux pas ici mettre ces fonctions dans un before each sinon il va y avoir un problème pour les tests du parent dont la liste doit être vide.
    @Override
    protected void initModels() {
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
    }

    @Override
    protected void init() {
        repository = medicalRecordRepository;
        model1 = EntityDataTest.createMedicalRecord1();
        model1Updated = EntityDataTest.createMedicalRecord1Updated();
        id1UpdatedExist = EntityDataTest.createMedicalRecordId1UpdatedExist();
        id1UpdatedNotExist = EntityDataTest.createMedicalRecordId1UpdatedNotExist();
        model2 = EntityDataTest.createMedicalRecord2();
        id2 = EntityDataTest.createMedicalRecordId2();
        model3 = EntityDataTest.createMedicalRecord3();
    }
    
    @Test
    void containsIdTest() {
        // given
        initModels();

        // when

        // then
        assertThat(medicalRecordRepository.containsId(id1UpdatedExist, model1)).isTrue(); 
        assertThat(medicalRecordRepository.containsId(id1UpdatedNotExist, model1)).isFalse();
        assertThat(medicalRecordRepository.containsId(new String[]{}, model1)).isFalse();
    }

    @Test
    @DisplayName("Find a medical record by unique key, record exists")
    void findMedicalRecordByUniqueKeyExistsTest() {
        // given
        initModels();

        // when
        Optional<MedicalRecord> result = medicalRecordRepository.findMedicalRecordByUniqueKey(id1UpdatedExist);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(model1);
    }    
    
    @Test
    @DisplayName("Can't find a medical record by unique key, record does not exist")
    void findMedicalRecordByUniqueKeyNotExistsTest() {
        // given
        initModels();

        // when
        Optional<MedicalRecord> result = medicalRecordRepository.findMedicalRecordByUniqueKey(id1UpdatedNotExist);

        // then
        assertThat(result).isNotPresent();
    }
    
    @Test
    @DisplayName("Find birthdate by unique key, record exists")
    void findBirthdateByUniqueKeyExistsTest() {
        // given
        initModels();

        // when
        String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(id1UpdatedExist);

        // then
        assertThat(birthdate).isNotBlank();
        assertThat(birthdate).isEqualTo(model1.getBirthdate());
    }

    @Test
    @DisplayName("Can't find birthdate by unique key, record does not exist")
    void findBirthdateByUniqueKeyNotExistsTest() {
        // given
        initModels();

        // when
        String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(id1UpdatedNotExist);

        // then
        assertThat(birthdate).isEmpty();
    }    
    
}
