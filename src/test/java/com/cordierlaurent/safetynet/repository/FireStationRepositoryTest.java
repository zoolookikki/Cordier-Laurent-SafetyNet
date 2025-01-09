package com.cordierlaurent.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.FireStation;

public class FireStationRepositoryTest extends CrudRepositoryTest<FireStation> {

    private FireStationRepository fireStationRepository = new FireStationRepository();
    
    // je ne peux pas ici mettre ces fonctions dans un before each sinon il va y avoir un problème pour les tests du parent dont la liste doit être vide.
    @Override
    protected void initModels() {
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
    }

    @Override
    protected void init() {
        repository = fireStationRepository;
        model1 = EntityDataTest.createFireStation1();
        model1Updated = EntityDataTest.createFireStation1Updated();
        id1UpdatedExist = EntityDataTest.createFireStationId1UpdatedExist();
        id1UpdatedNotExist = EntityDataTest.createFireStationId1UpdatedNotExist();
        model2 = EntityDataTest.createFireStation2();
        id2 = EntityDataTest.createFireStationId2();
        model3 = EntityDataTest.createFireStation3();
    }

    @Test
    void containsIdTest() {
        // given
        initModels();
        
        // when 
        
        // then

        // address exist
        assertThat(fireStationRepository.containsId(id1UpdatedExist, model1)).isTrue(); 
        // address not exist.
        assertThat(fireStationRepository.containsId(id1UpdatedNotExist, model1)).isFalse();
        // bad id.
        assertThat(fireStationRepository.containsId(new String[]{}, model1)).isFalse(); 
    }

    @Test
    @DisplayName("Delete an existing firestation by address")
    void deleteFireStationByAddressTest() {
        // given
        initModels();
        
        // when
        boolean result = fireStationRepository.deleteByAddress(id2[0]);
        List<FireStation> models = fireStationRepository.getModels();
                
        // then
        assertThat(result).isTrue();
        assertThat(models)
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(model1, model3); 
       
    }

    @Test
    @DisplayName("Delete a firestation who doesn't exist with address")
    void deleteFireStationByAddressFailTest() {
        // given
        initModels();
        
        // when
        boolean result = fireStationRepository.deleteByAddress(id1UpdatedNotExist[0]); 
                
        // then
        assertThat(result).isFalse();
       
    }
    
    
    @Test
    @DisplayName("Delete an existing firestation by station")
    void deleteFireStationByStationTest() {
        // given
        initModels();
        
        // when
        boolean result = fireStationRepository.deleteByStation(EntityDataTest.getStationExist());
        List<FireStation> models = fireStationRepository.getModels();
                
        // then
        assertThat(result).isTrue();
        assertThat(models)
            .isNotNull()
            .hasSize(1)
            .containsExactlyInAnyOrder(model2); 
       
    }

    @Test
    @DisplayName("Delete a firestation who doesn't exist by station")
    void deleteFireStationByStationFailTest() {
        // given
        initModels();
        
        // when
        boolean result = fireStationRepository.deleteByStation(EntityDataTest.getStationNotExist()); 
                
        // then
        assertThat(result).isFalse();
       
    }
    
    @Test
    @DisplayName("Find a existing firestation by address")
    void findStationByAddressTest() {
        // given
        initModels();

        // when
        int station = fireStationRepository.findStationByAddress(model1.getAddress());

        // then
        assertThat(station).isGreaterThan(0);
        assertThat(station).isEqualTo(model1.getStation());
    }
    
    @Test
    @DisplayName("Can't find a firestation by address")
    void findStationByAddressFailTest() {
        // given
        initModels();

        // when
        int station = fireStationRepository.findStationByAddress(id1UpdatedNotExist[0]);

        // then
        assertThat(station).isEqualTo(0);   
    }
    
    @Test
    @DisplayName("Find a existing firestation by address (ignorecase)")
    void findStationByAddressIgnoreCaseTest() {
        // given
        initModels();

        // when
        int station = fireStationRepository.findStationByAddress(model1.getAddress().toUpperCase());

        // then
        assertThat(station).isEqualTo(model1.getStation());
    }
    
    @Test
    @DisplayName("Find addresses by a existing station")
    void findAddressesByStationTest() {
        // given
        initModels();

        // when
        List<String> addresses = fireStationRepository.findAddressesByStationNumber(model1.getStation());

        // then
        assertThat(addresses)
            .isNotNull()
            .hasSize(2)
            .containsExactly(model1.getAddress(), model3.getAddress());
    }
    
    @Test
    @DisplayName("Can't find addresses by station")
    void findAddressesByStationFailTest() {
        // given
        initModels();

        // when
        List<String> addresses = fireStationRepository.findAddressesByStationNumber(EntityDataTest.getStationNotExist()); 

        // then
        assertThat(addresses).isEmpty();
    }
    
}
