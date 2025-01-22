package com.cordierlaurent.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.Util.ResponseEntityUtil;
import com.cordierlaurent.safetynet.dto.FireDTO;
import com.cordierlaurent.safetynet.dto.PersonsCoveredByFireStationDTO;
import com.cordierlaurent.safetynet.dto.Views;
import com.cordierlaurent.safetynet.service.FireStationService;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;

/**
 * Controller responsible for handling information requests related to FireStations.
 */
@RestController
@Log4j2
@Validated 
public class FireStationInfosController {

    @Autowired
    private FireStationService fireStationService;
    
    /**
     * Retrieves the list of persons covered by a station number (/firestation?stationNumber=&lt;station_number&gt;)
     * 
     * @param stationNumber The number of the fireStation.
     * @return a JSON response containing the list of persons and additional statistics (a count of the number of adults (greater than 18 years) and children (less than or equal 18 years)).
     */
    // the route is indicated here because for each endpoint, it is different.
    @GetMapping("/firestation")
    @JsonView(Views.Address.class)
    public ResponseEntity<?> getPersonsCoveredByFireStation(
            @RequestParam("stationNumber") 
            @Min(value = 1, message = "Station number must be greater than 0") int stationNumber){

        log.debug("GET/getPersonsCoveredByFireStation : key=" + stationNumber);
        
        PersonsCoveredByFireStationDTO personsCoveredByFireStationDTO = fireStationService.findPersonsCoveredByFireStation(stationNumber);
        
        log.debug("personsCoveredByFireStationDTO="+personsCoveredByFireStationDTO);

        return ResponseEntityUtil.response(personsCoveredByFireStationDTO, 
                personsCoveredByFireStationDTO.getPersonInformationsDTO(), 
                "List of people corresponding to the station "+stationNumber);
    }
    /*
    Example result :
    {
        "persons": [
        {
          "firstName": "xxxx",
          "lastName": "xxxx",
          "address": "xxxx",
          "phone": "xxxx"
        },
        {
          "firstName": "yyyy",
          "lastName": "yyyy",
          "address": "yyyy",
          "phone": "yyyy"
        }
      ],
      "numberOfAdults": ?,
      "numberOfChildren": ?
    }
    */

    
    /**
     * Retrieves the list of people living at a given address with the number of the fire station serving it (/fire?address=&lt;address&gt;).
     * 
     * This list must also include the telephone number, age and medical history of each person.
     * 
     * @param address the required address to search for.
     * @return a JSON response containing for the associated FireStation, the list of persons.
     */
    // the route is indicated here because for each endpoint, it is different.
    @GetMapping("/fire")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<?> getFireByAddresse(
            @RequestParam("address") 
            @NotBlank(message = "Address is required") String address){

        log.debug("GET/getFireByAddresse : key=" + address);
        
        FireDTO fireDTO = fireStationService.findFireByaddress(address);

        return ResponseEntityUtil.response(fireDTO, 
                fireDTO.getPersons(), 
                "List of people living at the address "+address);
    }
    /*
    Example result :
    {
      "station": ?,
      "persons": [
        {
          "firstName": "xxx",
          "lastName": "xxx",
          "phone": "???-???-????",
          "age": ??,
          "medications": ["xxxx", "xxxx"],
          "allergies": ["xxxx", "xxxx"]
        },
        {
          "firstName": "yyyy",
          "lastName": "yyyy",
          "phone": "???-???-????",
          "age": ??,
          "medications": ["yyyy", "yyyy"],
          "allergies": ["yyyy", "yyyy"]
        }
      ]
    }
    */
    
}
