package com.cordierlaurent.safetynet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.Util.ResponseEntityUtil;
import com.cordierlaurent.safetynet.dto.ChildAlertDTO;
import com.cordierlaurent.safetynet.dto.FloodAlertDTO;
import com.cordierlaurent.safetynet.dto.Views;
import com.cordierlaurent.safetynet.service.AlertService;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
//@Validated Active la validation sur les paramètres avec @PathVariable et @RequestParam (voir @NotBlank etc... dans les paramètres de chaque méthode)
@Validated 
public class AlertController {

    @Autowired
    AlertService alertService;
    
    // implémentation de l'url qui retourne la liste des enfants <= 18 ans à une adresse donnée avec les membres du foyer : http://localhost:8080/childAlert?address=<address>
    /*
    Rechercher la liste des personnes habitant à cette adresse.
    Pour chaque personne :
        Trouver l'age via la clef unique dans MedicalRecord
        Si moins de 18 ans 
            Ajouter dans la nouvelle DTO (prénom,nom,age).
            Complêter la DTO avec la liste des membres du foyer (ayant donc la même adresse). Attention à exclure la personne en cours de traitement.
    Renvoyer un json sous la forme :
    {
        "children": [
            {
                "firstName": "wwww",
                "lastName": "xxxx",
                "age": ?,
                "householdMembers": [
                    {
                        "firstName": "aaaa",
                        "lastName": "bbbb",
                        "address": "cccc",
                        "phone": "dddd"
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff",
                        "address": "gggg",
                        "phone": "hhhh"
                    }
                ]
            },
            {
                "firstName": "yyyy",
                "lastName": "zzzz",
                "age": ?,
                "householdMembers": [
                    {
                        "firstName": "aaaa",
                        "lastName": "bbbb",
                        "address": "cccc",
                        "phone": "dddd"
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff",
                        "address": "gggg",
                        "phone": "hhhh"
                    }
                ]
            }
        ]
    }
    */
    
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture + route childAlert.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/childAlert")
    @JsonView(Views.Address.class)
    public ResponseEntity<?> getChildAlert(
            @RequestParam("address") 
            @NotBlank(message = "L'adresse est obligatoire") String address){

        log.debug("GET/getChildAlert : key=" + address);

        List<ChildAlertDTO> childAlertDTOs = alertService.findChilddByAddress(address);
        
        return ResponseEntityUtil.response(childAlertDTOs, "Liste des enfants <= 18 ans habitant à l'adresse " + address);
    }
    
    
    // implémentation de l'url qui retourne la liste des numéros de téléphone des personnes couvertes par une station de pompiers, pour envoyer des sms. : http://localhost:8080/phoneAlert?firestation=<firestation_number>
    /*
    recherche les adresses par station => liste d'adresses.
    recherche personnes par adresse => liste de personnes.
    dans cette liste de personnes il ne faut avoir que le téléphone.
    attention aux doublons de numéro de téléphone => liste de type Set.
    la liste doit être triée => TreeSet.
    Renvoyer un json sous la forme :
    {
        [
        "???-???-????",
        "???-???-????",
        "???-???-????",
        "???-???-????",
        "???-???-????"
        ]
    }
    */
    
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture + route phoneAlert.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/phoneAlert")
    public ResponseEntity<?> getPhoneAlert(
            @RequestParam("firestation")
            @Min(value = 1, message = "Le numéro de station doit être supérieur à 0") int fireStation){

        log.debug("GET/getPhoneAlert : key=" + fireStation);

        // pas besoin de DTO ici car structure du fichier Json à renvoyer simple.
        List<String> phoneNumbers = alertService.findPhoneNumbersdByFireStation(fireStation);
        return ResponseEntityUtil.response(phoneNumbers, "Liste des numéros de téléphone des personnes couvertes par la station " + fireStation);
    }
    
    // implémentation de l'url qui retourne une liste des foyers desservis par des casernes de pompiers en cas d'inondation : http://localhost:8080/flood/stations?stations=<a list of station_numbers>
    /*
    création de la DTO FloodAlertDTO ((station, liste de FloodHouseoldDTO)
    pour chaque station
        création de la DTO FloodHouseoldDTO (address, liste de PersonHealthInformationsDTO)
        recherche des adresses de la station => liste d'adresses.
        pour chaque adresses
            recherche personnes par adresse => liste de personnes.
            création de la DTO PersonHealthInformations
            pour chaque personne
                trouver la date de naissance et les antécédents médicaux via la clef unique dans MedicalRecord
                calculer l'age 
                ajouter à la DTO PersonHealthInformationsDTO
            si la DTO PersonHealthInformationsDTO n'est pas vide
                ajouter à la DTO FloodHouseoldDTO
        si la DTO FloodHouseoldDTO n'est pas vide
            ajouter à la DTO FloodAlertDTO 

    Renvoyer un json sous la forme :
    [
      {
        "station": ?,
        "households": [
          {
            "address": "xxx 1",
            "persons": [
              {
                "firstName": "xxx",
                "lastName": "xxx",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["xxx", "xxx"],
                "allergies": ["xxx", "xxx"]
              },
              {
                "firstName": "xxx",
                "lastName": "xxx",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["xxx", "xxx"],
                "allergies": ["xxx", "xxx"]
              }
            ]
          },
          {
            "address": "xxx 2",
            "persons": [
              {
                "firstName": "xxx",
                "lastName": "xxx",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["xxx", "xxx"],
                "allergies": ["xxx", "xxx"]
              },
              {
                "firstName": "xxx",
                "lastName": "xxx",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["xxx", "xxx"],
                "allergies": ["xxx", "xxx"]
              }
            ]
          }
        ]
      },
      {
        "station": ?,
        "households": [
          {
            "address": "yyy 1",
            "persons": [
              {
                "firstName": "yyy",
                "lastName": "yyy",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["yyy", "yyy"],
                "allergies": ["yyy", "yyy"]
              },
              {
                "firstName": "yyy",
                "lastName": "yyy",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["yyy", "yyy"],
                "allergies": ["yyy", "yyy"]
              }
            ]
          },
          {
            "address": "yyy 2",
            "persons": [
              {
                "firstName": "yyy",
                "lastName": "yyy",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["yyy", "yyy"],
                "allergies": ["yyy", "yyy"]
              },
              {
                "firstName": "yyy",
                "lastName": "yyy",
                "phone": "???-???-????",
                "age": ??,
                "medications": ["yyy", "yyy"],
                "allergies": ["yyy", "yyy"]
              }
            ]
          }
        ]
      }
    ]
    */
    // @GettMapping : mappe une requête HTTP GET à une méthode de contrôleur : lecture + route phoneAlert.
    // @RequestParam : pour récupérer le paramètre passé en ? (ou & si plusieurs).
    @GetMapping("/flood/stations")
    @JsonView(Views.Detailed.class) 
    public ResponseEntity<?> getFloodAlert(
            @RequestParam("stations") 
            @NotEmpty(message = "La liste des stations ne peut pas être vide") List<Integer> stations){

        log.debug("GET/getFloodAlert : key=" + stations);

        // Vérification pour filtrer les valeurs nulles ==> filtrage @notEmpty insuffisant si ajout de blancs après stations=
        if (stations == null || stations.isEmpty() || stations.contains(null)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La liste des stations est obligatoire et doit contenir uniquement des entiers valides");
        }
        List<FloodAlertDTO> floodAlertDTOs = alertService.findFloodByStations(stations);
        
        // Sérialisation explicite pour débogage
/*        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Detailed.class));
            String serialized = objectMapper.writeValueAsString(floodAlertDTOs);
            log.debug("Serialized output: " + serialized);
        } catch (JsonProcessingException e) {
            log.error("Error serializing FloodAlertDTO: ", e);
        }        
        log.debug("Returning response: " + floodAlertDTOs);
*/        
        return ResponseEntityUtil.response(floodAlertDTOs, "Liste des foyers desservis par les stations " + stations);
    }

}

