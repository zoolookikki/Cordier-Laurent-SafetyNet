package com.cordierlaurent.safetynet.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// .... à revoir pourquoi cela ne s'importe pas automatiquement :
// ne s'est pas importé automatiquement avec Eclipse 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.dto.FireDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.FireStationService;

//pour ne charger que la partie concernant le MVC et précision de la classe pour ne charger que ce contrôleurs pour les tests.
@WebMvcTest(FireStationInfosController.class)
public class FireStationInfosControllerTest {

    // car MockMvc n'est pas automatiquement configuré dans un test unitaire @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;

//  @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    @MockitoBean
    private FireStationService fireStationService;
    
    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    private static FireStation fireStation1;
    
   @BeforeAll
    private static void setUp() {
        fireStation1 = EntityDataTest.createFireStation1();
    }

    @Test
    @DisplayName("Url which requests the list of people corresponding to a station")
    void getPersonsCoveredByFireStationTest() throws Exception {

        // Given
        // mock(...) => création de la DTO factice.
        when(fireStationService.findPersonsCoveredByFireStation(fireStation1.getStation())).thenReturn(mock(PersonsCoveredByFireStationDTO.class));

        // When & Then
        mockMvc.perform(get("/firestation")
                .param("stationNumber", String.valueOf(fireStation1.getStation())))
            .andExpect(status().isOk());

        verify(fireStationService, times(1)).findPersonsCoveredByFireStation(fireStation1.getStation());
    }    
    
 @Test
    @DisplayName("Url which returns the people living at a given address with the number of the fire station serving it")
    void getFireByAddressTest() throws Exception {
     
        // Given
        // mock(...) => création de la DTO factice.
        when(fireStationService.findFireByaddress(fireStation1.getAddress())).thenReturn(mock(FireDTO.class));

        // When & Then
        mockMvc.perform(get("/fire")
                .param("address", fireStation1.getAddress()))
            .andExpect(status().isOk());

        verify(fireStationService, times(1)).findFireByaddress(fireStation1.getAddress());
    } 
 
}
