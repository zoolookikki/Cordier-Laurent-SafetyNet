package com.cordierlaurent.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;

// inutile ici mais par sécurité pour le moment je le laisse.
@SpringBootTest
public class PersonRepositoryTest {

    private PersonRepository personRepository;
    private static Person person1;
    private static Person person1Updated;
    private static Person person2;
    private static Person person3;

    @BeforeAll
    private static void setUp() {
        person1 = EntityDataTest.createPerson1();
        person1Updated = EntityDataTest.createPerson1Updated();
        person2 = EntityDataTest.createPerson2();
        person3 = EntityDataTest.createPerson3();
    }
    
    @BeforeEach
    private void setUpPerTest() {
        personRepository = new PersonRepository();
    }
    
    @AfterAll
    private static void tearDown(){
    }
    
    
    
    @Test
//    @Disabled
    @DisplayName("Add persons and verifies that the returned list is up to date")
    void getPersonsTest() {

        // given
        personRepository.addModel(person1);
        personRepository.addModel(person2);
        
        // when
        List<Person> persons = personRepository.getModels();
        
        // then
        assertThat(persons).isNotNull()
            .hasSize(2)
            // je préfère utiliser containsExactlyInAnyOrder à containsExactly car si je change de type de list, le test va échoué et l'ordre n'a pas d'importance ici.
            // le test est suffisant car equals qui est généré par @Data.
            .containsExactlyInAnyOrder(person1, person2); 
    }

    @Test
    @DisplayName("Replaces the existing list with a new list")
    void setPersonsTest() {
         
         // given
         personRepository.addModel(person1);
         personRepository.addModel(person2);

         //when
         List<Person> personsNewList = new ArrayList<Person>();
         personsNewList.add(person3);
         personRepository.setModels(personsNewList);

         //then
         List<Person> persons = personRepository.getModels();
         assertThat(persons)
             .isNotNull()
             .hasSize(1)
             .containsExactly(person3); 
        
    }

    @Test
    @DisplayName("A person has been added")
    void addtPersonsTest() {

        // given : repository empty.

        // when
        personRepository.addModel(person2);
        
        //then
        List<Person> persons = personRepository.getModels();
        assertThat(persons)
            .isNotNull()
            .hasSize(1)
            .containsExactly(person2);

    }
    
    @Test
    @DisplayName("Update of a person who exists with the unique key first name+last name")
    void updatePersonsTest() {
        
        // given
        personRepository.addModel(person1);
        
        // when
        boolean result = personRepository.updateModelByUniqueKey( new String[]{"John", "Boyd"}, person1Updated);
        List<Person> persons = personRepository.getModels();
        
        // then
        assertThat(result).isTrue();
        assertThat(persons)
            .isNotNull()
            .hasSize(1)
            .containsExactly(person1Updated);
        
    }
        
    @Test
    @DisplayName("Update of a person who does not exist with the unique key first name+last name")
    void updatePersonsFailTest() {
        
        // given
        personRepository.addModel(person1);
        personRepository.addModel(person2);
        personRepository.addModel(person3);
        
        // when
        boolean result = personRepository.updateModelByUniqueKey (new String[]{"xxxxx", "Boyd"}, person1Updated);
                
        // then
        assertThat(result).isFalse();
       
    }
    
    @Test
    @DisplayName("Delete an existing person with the unique key first name+last name")
    void deletePersonsTest() {
        
        // given
        personRepository.addModel(person1);
        personRepository.addModel(person2);
        personRepository.addModel(person3);
        
        // when
        boolean result = personRepository.deleteModelByUniqueKey(new String[]{"Jacob", "Boyd"}); // the number 2
        List<Person> persons = personRepository.getModels();
                
        // then
        assertThat(result).isTrue();
        assertThat(persons)
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(person1, person3); 
       
    }
    
    @Test
    @DisplayName("Delete a person who doesn't exist with the unique key first name+last name")
    void deletePersonsFailTest() {
        
        // given
        personRepository.addModel(person1);
        personRepository.addModel(person2);
        personRepository.addModel(person3);
        
        // when
        boolean result = personRepository.deleteModelByUniqueKey(new String[] {"xxxxx", "Boyd"}); 
                
        // then
        assertThat(result).isFalse();
       
    }

}
