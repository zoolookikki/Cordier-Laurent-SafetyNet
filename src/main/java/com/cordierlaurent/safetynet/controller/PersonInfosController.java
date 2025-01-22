package com.cordierlaurent.safetynet.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cordierlaurent.safetynet.Util.ResponseEntityUtil;
import com.cordierlaurent.safetynet.dto.PersonInformationsDTO;
//import com.cordierlaurent.safetynet.dto.PersonHealthExtentedInformationsDTO;
import com.cordierlaurent.safetynet.dto.Views;
import com.cordierlaurent.safetynet.service.PersonService;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for retrieving detailed information about persons.
 * 
 */
@RestController
@Log4j2
@Validated 
public class PersonInfosController {
    
    @Autowired
    private PersonService personService;

    /**
     * Common logic for retrieving detailed persons informations by last name.
     * 
     * @param lastName the last name to filter persons by.
     * @return a JSON response containing the list of persons with detailed information. 
     */
    private ResponseEntity<?> getCommonPersonInfoLastName(String lastName) {
        List<PersonInformationsDTO> personInformationsDTOs = personService.findPersonInfoByLastName(lastName);
        return ResponseEntityUtil.response(personInformationsDTOs, "List of detailed information of persons with the name "+ lastName);
     }

    /**
     * Returns the list of detailed information of persons with a given last name (/personInfolastName=<lastName>).
     * 
     * @param lastName the required last name to filter persons by. 
     * @return a JSON response containing the list of persons with detailed information. 
     */
    // Warning, i managed 2 cases of URL because the one requested in the specifications is not usual : (nok) /personInfolastName=<lastName>
    @GetMapping("/personInfolastName={lastName}")
    @JsonView(Views.WithEmail.class)
    public ResponseEntity<?> getPersonInfoByPath(
            @PathVariable("lastName") 
            @NotBlank(message = "Lastname is required") String lastName) {

        log.debug("GET/getPersonInfoByPath : key=" + lastName);

        return getCommonPersonInfoLastName(lastName);
    }
    /**
     * Returns the list of detailed information of persons with a given last name via a query parameter (/personInfo?lastName=&lt;lastName&gt;).
     * 
     * @param lastName the required last name to filter persons by. 
     * @return a JSON response containing the list of persons with detailed information. 
     */
    // Warning, i managed 2 cases of URL because the one requested in the specifications is not usual : (ok) /personInfo?lastName=<lastName>
    @GetMapping("/personInfo")
    @JsonView(Views.WithEmail.class)
    public ResponseEntity<?> getPersonInfoByRequest(
            @RequestParam("lastName") 
            @NotBlank(message = "Lastname is required") String lastName) {

        log.debug("GET/getPersonInfoByRequest : key=" + lastName);

        return getCommonPersonInfoLastName(lastName);
    }
    /*
    Example result :
    [
        {
            "firstName": "xxxx",
            "lastName": "xxxx",
            "address": "xxxx",
            "age": ??,
            "email": "xxxx",
            "medications": ["xxxx", "xxxx"],
            "allergies": ["xxxx", "xxxx"],
        },
        {
            "firstName": "yyyy",
            "lastName": "yyyy",
            "address": "yyyy",
            "age": ??,
            "email": "yyyy",
            "medications": ["yyyy", "yyyy"],
            "allergies": ["yyyy", "yyyy"],
        }
    ]
    */

    /**
     * Returns the list of email addresses of all residents of a given city (/communityEmail?city=&lt;city&gt;).
     *
     * @param city the required name of the city filter residents by. 
     * @return a JSON response containing the list of email addresses of all residents in the specified city
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmails(
            @RequestParam("city") 
            @NotBlank(message = "The city name is required") String city){

        log.debug("GET/getCommunityEmails : key=" + city);

        Set<String> emails = personService.findEmailsByCity(city);        
        return ResponseEntityUtil.response(emails, "List of email addresses of everyone in town " + city);
    }
    /*
    Example result :
    [
      "xxxx@xxxx.com",
      "yyyy@yyyy.com",
      "zzzz@zzzz.com",
    ]
    */
    
}
