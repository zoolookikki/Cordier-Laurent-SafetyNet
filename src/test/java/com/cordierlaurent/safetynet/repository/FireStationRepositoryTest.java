package com.cordierlaurent.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.FireStation;

public class FireStationRepositoryTest extends CrudRepositoryTest<FireStation> {

    private FireStationRepository fireStationRepository = new FireStationRepository();
    
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
    @DisplayName("Delete an existing firestation by address")
    void deleteFireStationByAddressTest() {
        
        // given
        fireStationRepository.addModel(model1);
        fireStationRepository.addModel(model2);
        fireStationRepository.addModel(model3);
        
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
        fireStationRepository.addModel(model1);
        fireStationRepository.addModel(model2);
        fireStationRepository.addModel(model3);
        
        // when
        boolean result = fireStationRepository.deleteByAddress(id1UpdatedNotExist[0]); 
                
        // then
        assertThat(result).isFalse();
       
    }
    
    
    @Test
    @DisplayName("Delete an existing firestation by station")
    void deleteFireStationByStationTest() {
        
        // given
        fireStationRepository.addModel(model1);
        fireStationRepository.addModel(model2);
        fireStationRepository.addModel(model3);
        
        // when
        boolean result = fireStationRepository.deleteByStation(EntityDataTest.getStationForDeleteExist());
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
        fireStationRepository.addModel(model1);
        fireStationRepository.addModel(model2);
        fireStationRepository.addModel(model3);
        
        // when
        boolean result = fireStationRepository.deleteByStation(EntityDataTest.getSationForDeleteNotExist()); 
                
        // then
        assertThat(result).isFalse();
       
    }
    
    
}
