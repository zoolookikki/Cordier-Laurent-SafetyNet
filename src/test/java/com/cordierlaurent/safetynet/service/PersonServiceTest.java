package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@SpringBootTest
public class PersonServiceTest {

    @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    private PersonRepository personRepository;
    
    @Autowired
    private PersonService personService;
    
    @Test
    @DisplayName("A person has been added with success")
    public void createPersonTest(){
        
        // given
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.getPersons()).thenReturn(new ArrayList<Person>());
        
        // when
        boolean isCreated = personService.createPerson(person1);
        
        // then
        assertThat(isCreated).isTrue();
        verify(personRepository, times(1)).addPerson(person1);
    }
    
    @Test
    @DisplayName("Failure when creating a person that already exists")
    public void createDuplicatePerson() {
        
        // given
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
        Person person1Duplicated = person1;
        when(personRepository.getPersons()).thenReturn(List.of(person1, person2));
        
        // when
        boolean isCreated = personService.createPerson(person1);
        
        // then
        assertThat(isCreated).isFalse();
        verify(personRepository, never()).addPerson(person1Duplicated);
    }
    
}
