package com.cordierlaurent.safetynet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

// .... à revoir pourquoi cela ne s'importe pas automatiquement :
// ne s'est pas importé automatiquement avec Eclipse ==> méthode post(String) undefined 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.MessageService;
import com.cordierlaurent.safetynet.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

// pour ne charger que la partie concernant le MVC et précision de la classe pour ne charger que ce contrôleurs pour les tests.
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

//    @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).

    // car MockMvc n'est pas automatiquement configuré dans un test unitaire @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    // celui là m'a bien ennuyé => j'hésite entre le fait d'avoir @WebMvcTest et de mettre @SpringBootTest.
    @MockitoBean
    private MessageService messageService;
    
    @Test
    @DisplayName("A person has been added with success")
    public void createPersonTest () throws Exception {
     
        // given
        
        Person person1 = EntityDataTest.createPerson1();
        when(personService.isUnique(any(Person.class))).thenReturn(true);
        
        // when

        // Simule une requête HTTP POST avec un corps JSON.        
        ResultActions result = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                // convertir l'objet person1 en Json.
                .content(objectMapper.writeValueAsString(person1)));
        
        // then

        // status() pour matcher le statut de la réponse.
        // vérifie que le statut HTTP de la réponse est 201 Created.
        result.andExpect(status().isCreated())
            // vérifie les champs firstName et lastName dans le corps JSON de la réponse
            // ici je ne teste que les champs essentiels (clés uniques).
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Boyd"));         

        verify(personService, times(1)).addPerson(any(Person.class));
        verify(jsonDataRepository, times(1)).save();        
    }
    
    // A SUIVRE : implémentation partielle pour test => il y a encore du travail...
    
}
