package com.cordierlaurent.safetynet.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;

//.... à revoir pourquoi cela ne s'importe pas automatiquement :
//ne s'est pas importé automatiquement avec Eclipse ==> méthode post(String) undefined 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
//Par défaut, @SpringBootTest démarre le contexte complet de l'application, mais il ne configure pas automatiquement MockMvc.
//MockMvc est configuré automatiquement uniquement avec @WebMvcTest, qui charge un sous-ensemble des beans pour tester les contrôleurs.
@AutoConfigureMockMvc
public class FloodAlertControllerIT {

    private final static String JSON_FILENAME_ORIGINAL = "src/main/resources/data/data_original.json";

    @Autowired
    private MockMvc mockMvc;

    // for testing, load this json file instead of the default one.
    @DynamicPropertySource
    static void overrideJsonFileProperty(DynamicPropertyRegistry registry) {
        registry.add("safetynet.json-file", () -> JSON_FILENAME_ORIGINAL);
    }

    // normal result.
    /*
    [
        {
            "station": 1,
            "households": [
                {
                    "address": "908 73rd St",
                    "persons": [
                        {
                            "firstName": "Reginold",
                            "lastName": "Walker",
                            "phone": "841-874-8547",
                            "age": 45,
                            "medications": [
                                "thradox:700mg"
                            ],
                            "allergies": [
                                "illisoxian"
                            ]
                        },
                        {
                            "firstName": "Jamie",
                            "lastName": "Peters",
                            "phone": "841-874-7462",
                            "age": 42,
                            "medications": [],
                            "allergies": []
                        }
                    ]
                },
                {
                    "address": "947 E. Rose Dr",
                    "persons": [
                        {
                            "firstName": "Brian",
                            "lastName": "Stelzer",
                            "phone": "841-874-7784",
                            "age": 49,
                            "medications": [
                                "ibupurin:200mg",
                                "hydrapermazol:400mg"
                            ],
                            "allergies": [
                                "nillacilan"
                            ]
                        },
                        {
                            "firstName": "Shawna",
                            "lastName": "Stelzer",
                            "phone": "841-874-7784",
                            "age": 44,
                            "medications": [],
                            "allergies": []
                        },
                        {
                            "firstName": "Kendrik",
                            "lastName": "Stelzer",
                            "phone": "841-874-7784",
                            "age": 10,
                            "medications": [
                                "noxidian:100mg",
                                "pharmacol:2500mg"
                            ],
                            "allergies": []
                        }
                    ]
                },
                {
                    "address": "644 Gershwin Cir",
                    "persons": [
                        {
                            "firstName": "Peter",
                            "lastName": "Duncan",
                            "phone": "841-874-6512",
                            "age": 24,
                            "medications": [],
                            "allergies": [
                                "shellfish"
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "station": 2,
            "households": [
                {
                    "address": "951 LoneTree Rd",
                    "persons": [
                        {
                            "firstName": "Eric",
                            "lastName": "Cadigan",
                            "phone": "841-874-7458",
                            "age": 79,
                            "medications": [
                                "tradoxidine:400mg"
                            ],
                            "allergies": []
                        }
                    ]
                },
                {
                    "address": "892 Downing Ct",
                    "persons": [
                        {
                            "firstName": "Sophia",
                            "lastName": "Zemicks",
                            "phone": "841-874-7878",
                            "age": 36,
                            "medications": [
                                "aznol:60mg",
                                "hydrapermazol:900mg",
                                "pharmacol:5000mg",
                                "terazine:500mg"
                            ],
                            "allergies": [
                                "peanut",
                                "shellfish",
                                "aznol"
                            ]
                        },
                        {
                            "firstName": "Warren",
                            "lastName": "Zemicks",
                            "phone": "841-874-7512",
                            "age": 39,
                            "medications": [],
                            "allergies": []
                        },
                        {
                            "firstName": "Zach",
                            "lastName": "Zemicks",
                            "phone": "841-874-7512",
                            "age": 7,
                            "medications": [],
                            "allergies": []
                        }
                    ]
                },
                {
                    "address": "29 15th St",
                    "persons": [
                        {
                            "firstName": "Jonanathan",
                            "lastName": "Marrack",
                            "phone": "841-874-6513",
                            "age": 36,
                            "medications": [],
                            "allergies": []
                        }
                    ]
                }
            ]
        }
    ]    
    */
    
    @Test
    @DisplayName("Get Flood Information for Stations - Integration Test")
    public void getFloodStationsIntegrationTest() throws Exception {

        // given
        List<String> stationNumbers = Arrays.asList("1", "2");

        // when then
        mockMvc.perform(get("/flood/stations")
                .param("stations", String.join(",", stationNumbers)) 
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // Check that there are exactly 2 stations
            .andExpect(jsonPath("$.length()").value(2)) 
            // Verify that there are 3 households in station 1
            .andExpect(jsonPath("$[0].households.length()").value(3)) 
            // Verify that there are 3 households in station 2
            .andExpect(jsonPath("$[1].households.length()").value(3)) 
            // Check addresses in station 1 without worrying about the order.
            .andExpect(jsonPath("$[0].households[*].address").value(containsInAnyOrder("644 Gershwin Cir", "908 73rd St", "947 E. Rose Dr"))) 
            // Check first name in station 1 without worrying about the order.
            .andExpect(jsonPath("$[0].households[*].persons[*].firstName").value(containsInAnyOrder("Brian", "Jamie", "Kendrik", "Peter", "Reginold", "Shawna")));
        // for medications, to be seen later because complicated.
    }
}
