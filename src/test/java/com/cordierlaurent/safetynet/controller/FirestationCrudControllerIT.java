package com.cordierlaurent.safetynet.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.FireStation;
import com.fasterxml.jackson.databind.ObjectMapper;

//.... à revoir pourquoi cela ne s'importe pas automatiquement :
//ne s'est pas importé automatiquement avec Eclipse ==> méthode post(String) undefined 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// Par défaut, @SpringBootTest démarre le contexte complet de l'application, mais il ne configure pas automatiquement MockMvc.
// MockMvc est configuré automatiquement uniquement avec @WebMvcTest, qui charge un sous-ensemble des beans pour tester les contrôleurs.
@AutoConfigureMockMvc
// Exceptionnellement car ici je teste un scénario de création/modification/suppression de CRUD.
// Sinon il faut réinitialiser l'état entre chaque test => pour gagner du temps de développement dans ce cas de figure.
// De plus, je n'ai pas besoin de contrôler les mises à jour du fichier json car les 3 étapes (création/modification/suppression) se contrôlent entre elles, si une échoue, les autres échouent.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirestationCrudControllerIT {

    private final static String JSON_FILENAME_TEST = "src/main/resources/data/FireStationControllerIT.json";
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static FireStation fireStation1;
    private static FireStation fireStation1Updated;

    // for testing, load this json file instead of the default one.
    @DynamicPropertySource
    static void overrideJsonFileProperty(DynamicPropertyRegistry registry) {
        registry.add("safetynet.json-file", () -> JSON_FILENAME_TEST);
    }

    @BeforeAll
    private static void setUp() {
       fireStation1 = EntityDataTest.createFireStation1();
       fireStation1Updated = EntityDataTest.createFireStation1Updated();
    }

   @Test
    @Order(1)
    public void createFirestationIntegrationTest() throws Exception {
       
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation1)))
            .andExpect(status().isCreated())
             // verification that we receive the object that we created.
            .andExpect(jsonPath("$.station").value(String.valueOf(fireStation1.getStation())))
            .andExpect(jsonPath("$.address").value(fireStation1.getAddress()));
    }
    @Test
    @Order(2)
    public void updateFirestationIntegrationTest() throws Exception {

        // if the creation failed, this test will fail.
        mockMvc.perform(put("/firestation/{param1}/{param2}", fireStation1.getAddress(), String.valueOf(fireStation1.getStation()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation1Updated)))
            .andExpect(status().isOk())
            // checking that in the object received, the fields are up to date.
            .andExpect(jsonPath("$.station").value(String.valueOf(fireStation1Updated.getStation()))) 
            .andExpect(jsonPath("$.address").value(fireStation1Updated.getAddress()));
    }
    
    @Test
    @Order(3)
    public void deleteFirestationIntegrationTest() throws Exception {

        // if the modification failed, this test will fail : warning the firestation must be the modified one.
        mockMvc.perform(delete("/firestation/{param1}/{param2}", fireStation1Updated.getAddress(), String.valueOf(fireStation1Updated.getStation())))
            .andExpect(status().isNoContent());
    }
    
}
