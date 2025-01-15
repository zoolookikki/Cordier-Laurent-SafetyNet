package com.cordierlaurent.safetynet.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// .... à revoir pourquoi cela ne s'importe pas automatiquement :
// ne s'est pas importé automatiquement avec Eclipse ==> méthode post(String) undefined 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.AlertService;
import com.cordierlaurent.safetynet.service.MessageService;

//pour ne charger que la partie concernant le MVC et précision de la classe pour ne charger que ce contrôleurs pour les tests.
@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    // car MockMvc n'est pas automatiquement configuré dans un test unitaire @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;

//  @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    @MockitoBean
    private AlertService alertService;

    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    // celui là m'a bien ennuyé => j'hésite entre le fait d'avoir @WebMvcTest et de mettre @SpringBootTest.
    @MockitoBean
    private MessageService messageService;

    @Test
    @DisplayName("Url which returns the list of children <= 18 years old at a given address with the members of the household")
    void getChildAlertTest() throws Exception {
        
        // given
        String address = "1509 Culver St";
        when(alertService.findChilddByAddress(address)).thenReturn(Collections.emptyList());

        // when then
        mockMvc.perform(get("/childAlert")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0)); 

        verify(alertService, times(1)).findChilddByAddress(address);        
    }
    
    @Test
    @DisplayName("Url which returns the list of telephone numbers of people covered by a fire station, to send SMS")
    void getPhoneAlertTest() throws Exception {

        // given
        int fireStation = 1;
        when(alertService.findPhoneNumbersdByFireStation(fireStation)).thenReturn(Collections.emptyList());

        // when then
        mockMvc.perform(get("/phoneAlert")
                .param("firestation", String.valueOf(fireStation)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(alertService, times(1)).findPhoneNumbersdByFireStation(fireStation);        

     }
    
    @Test
    @DisplayName("Url that returns a list of households served by fire stations in the event of floodings")
    void getFloodAlertTest() throws Exception {

        // given.
        List<Integer> stations = List.of(1, 2,3);
        when(alertService.findFloodByStations(stations)).thenReturn(Collections.emptyList());

        // when then
        mockMvc.perform(get("/flood/stations")
                .param("stations", "1,2,3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0)); 

        verify(alertService, times(1)).findFloodByStations(stations);     
    }

    @Test
    void getFloodAlertStationsNull() throws Exception {
        mockMvc.perform(get("/flood/stations"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void getFloodAlertStationsEmpty() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", ""))
            .andExpect(status().isBadRequest());
    }
    @Test
    void getFloodAlertStationsContainsNull() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "1,,3"))
            .andExpect(status().isBadRequest());
    }    
}
