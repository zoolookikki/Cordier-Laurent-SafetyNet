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

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.PersonService;

@WebMvcTest(PersonInfosController.class)
public class PersonInfosControllerTest {

    // because MockMvc is not automatically configured in a @WebMvcTest unit test.
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // replaces deprecated @MockBean
    private PersonService personService;
    
    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    @Test
    @DisplayName("Url by path that returns detailed information about people with a given last name")
    void getPersonInfoByPathTest() throws Exception {

        // given
        String lastName = "Boyd";
        when(personService.findPersonInfoByLastName(lastName)).thenReturn(Collections.emptyList());

        // when then
        mockMvc.perform(get("/personInfolastName={lastName}", lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(personService, times(1)).findPersonInfoByLastName(lastName);
    }
    
    @Test
    @DisplayName("Url by request that returns detailed information about people with a given last name")
    void getPersonInfoByRequestTest() throws Exception {

        // given
        String lastName = "Boyd";
        when(personService.findPersonInfoByLastName(lastName)).thenReturn(Collections.emptyList());

        // when then
        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(personService, times(1)).findPersonInfoByLastName(lastName);
    }
    
    @Test
    @DisplayName("url that returns the email addresses of all residents of a given city")
    void getCommunityEmailsTest() throws Exception {

        // given
        String city = "Culver";
        when(personService.findEmailsByCity(city)).thenReturn(Collections.emptySet());

        // when then
        mockMvc.perform(get("/communityEmail")
                .param("city", city))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(personService, times(1)).findEmailsByCity(city);
    }    
    
}
