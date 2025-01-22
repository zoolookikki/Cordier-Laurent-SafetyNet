package com.cordierlaurent.safetynet.controller;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// .... à revoir pourquoi cela ne s'importe pas automatiquement :
// ne s'est pas importé automatiquement avec Eclipse 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.FireStationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FireStationController.class)
public class FireStationControllerTest extends CrudControllerTest<FireStation> {

    // because MockMvc is not automatically configured in a @WebMvcTest unit test.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // replaces deprecated @MockBean
    private FireStationService fireStationService;
    
    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    private static FireStation fireStation1;
    private static FireStation fireStation1Updated;
    
    @Override
    protected String getBaseUrl() {
        return "/firestation";
    }

    @Override
    protected FireStation getValidModel() {
        return fireStation1;
    }

    @Override
    protected FireStation getUpdatedModel() {
        return fireStation1Updated;
    }

    @Override
    protected CrudService<FireStation> getMockService() {
        return fireStationService;
    }

    @Override
    protected String[] getIdForUpdate() {
        return new String[]{fireStation1.getAddress(), String.valueOf(fireStation1.getStation())};
    }    
    
    @BeforeAll
    private static void setUp() {
        fireStation1 = EntityDataTest.createFireStation1();
        fireStation1Updated = EntityDataTest.createFireStation1Updated();
    }

    @Test
    void updateModelOrDeleteWithInvalidStationNumberTest() throws Exception {

        mockMvc.perform(put(getBaseUrl() + "/{param1}/{param2}", fireStation1.getAddress(), String.valueOf(EntityDataTest.getStation0Invalid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation1)))
            .andExpect(status().isBadRequest());

        mockMvc.perform(delete(getBaseUrl() + "/{param1}/{param2}", fireStation1.getAddress(), String.valueOf(EntityDataTest.getStation0Invalid())))
            .andExpect(status().isBadRequest());
    }
    
}
