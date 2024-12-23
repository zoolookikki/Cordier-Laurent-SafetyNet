package com.cordierlaurent.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.PersonRepository;

@SpringBootTest
public class PersonServiceTest {
    
    private static Person person1;
    private static Person person2;
    private static Person person3;

    @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @BeforeAll
    private static void setUp() {
        person1 = EntityDataTest.createPerson1();
        person2 = EntityDataTest.createPerson2();
        person3 = EntityDataTest.createPerson3();
    }

    @Test
    @DisplayName("A person is unique")
    public void isUniquePersonTest(){
        
        // given
        when(personRepository.getPersons()).thenReturn(List.of(person1, person2));
        
        // when
        boolean isUnique = personService.isUnique(person3);
        
        // then
        assertThat(isUnique).isTrue();
    }
    
    @Test
    @DisplayName("A person is not unique")
    public void isNotUniquePersonTest(){
        
        // given
        when(personRepository.getPersons()).thenReturn(List.of(person1, person2));
        
        // when
        boolean isUnique = personService.isUnique(person2);
        
        // then
        assertThat(isUnique).isFalse();
    }
}
