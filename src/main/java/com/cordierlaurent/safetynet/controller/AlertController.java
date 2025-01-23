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

/**
 * Controller responsible for handling information requests related to all alerts.
 */
@RestController
@Log4j2
@Validated 
public class AlertController {

    @Autowired
    AlertService alertService;
    
    /**
     * Returns a list of children (less than or equal 18 years old) living at a given address with members of their household (/childAlert?address=&lt;address&gt;).
     *
     * @param address the required address for which to retrieve the information.
     * @return a JSON response containing the list of children, and for each child, the list of household members.
     */
    @GetMapping("/childAlert")
    @JsonView(Views.Basic.class)
    public ResponseEntity<?> getChildAlert(
            @RequestParam("address") 
            @NotBlank(message = "Address is required") String address){

        log.debug("GET/getChildAlert : key=" + address);

        List<ChildAlertDTO> childAlertDTOs = alertService.findChilddByAddress(address);
        
        return ResponseEntityUtil.response(childAlertDTOs, "List of children <= 18 years old living at the address" + address);
    }
    /*
    Example result :
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
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff"
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
                        "lastName": "bbbb"
                    },
                    {
                        "firstName": "eeee",
                        "lastName": "ffff"  
                    }
                ]
            }
        ]
    }
    */

    /**
     * Returns the list of phone numbers of people covered by a given fire station (/phoneAlert?firestation=&lt;firestation_number&gt;).
     *
     * @param fireStation the station number.
     * @return a sorted list of phone numbers.
     */    
    @GetMapping("/phoneAlert")
    public ResponseEntity<?> getPhoneAlert(
            @RequestParam("firestation")
            @Min(value = 1, message = "Station number must be greater than 0") int fireStation){

        log.debug("GET/getPhoneAlert : key=" + fireStation);

        List<String> phoneNumbers = alertService.findPhoneNumbersdByFireStation(fireStation);
        return ResponseEntityUtil.response(phoneNumbers, "List of telephone numbers of people covered by the station " + fireStation);
    }
    /*    
    Example result :
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
    
    
    
   
    /**
     * Returns a list of homes served by fire stations in case of flooding (/flood/stations?stations=&lt;a list of station_numbers&gt;)
     *
     * @param stations list of station numbers (each requested station is separated by a comma).
     * @return a detailed list by station, of households and their inhabitants.
     */
    @GetMapping("/flood/stations")
    @JsonView(Views.Detailed.class) 
    public ResponseEntity<?> getFloodAlert(
            @RequestParam("stations") 
            @NotEmpty(message = "Station list cannot be empty") List<Integer> stations){

        log.debug("GET/getFloodAlert : key=" + stations);

        // Check to filter null values ​​because @notEmpty filtering is insufficient if adding blanks after stations=
        if (stations == null || stations.isEmpty() || stations.contains(null)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The station list is mandatory and must contain only valid integers");
        }
        List<FloodAlertDTO> floodAlertDTOs = alertService.findFloodByStations(stations);
        return ResponseEntityUtil.response(floodAlertDTOs, "List of households served by the stations " + stations);
    }
    /*
    Example result :
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

}

