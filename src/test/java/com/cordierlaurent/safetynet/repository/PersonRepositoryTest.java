package com.cordierlaurent.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;

public class PersonRepositoryTest extends CrudRepositoryTest<Person>{

    private PersonRepository personRepository = new PersonRepository();

    // je ne peux pas ici mettre ces fonctions dans un before each sinon il va y avoir un problème pour les tests du parent dont la liste doit être vide.
    @Override
    protected void initModels() {
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
    }

    @Override
    protected void init() {
        repository = personRepository;

        model1 = EntityDataTest.createPerson1();
        model1Updated = EntityDataTest.createPerson1Updated();
        id1UpdatedExist = EntityDataTest.createPersonId1UpdatedExist();
        id1UpdatedNotExist = EntityDataTest.createPersonId1UpdatedNotExist();
        model2 = EntityDataTest.createPerson2();
        id2 = EntityDataTest.createPersonId2();
        model3 = EntityDataTest.createPerson3();
    }
    
    @Test
    void containsIdTest() {
        // given
        initModels();
        
        // when 
        
        // then

        // firstName + lastName exist
        assertThat(personRepository.containsId(id1UpdatedExist, model1)).isTrue(); 
        // firstName + lastName not exist
        assertThat(personRepository.containsId(id1UpdatedNotExist, model1)).isFalse();
        // bad id.
        assertThat(personRepository.containsId(new String[]{}, model1)).isFalse(); 
    }

    @Test
    @DisplayName("Find persons by addresses, multiple matches found")
    void findPersonsByAddressesMatchTest() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByAddresses(EntityDataTest.getPersonAllAddressesMatch());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Can't find persons by addresses, no matches found")
    void findPersonsByAddressesMatchFailTest() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByAddresses(EntityDataTest.getPersonAllAddressesNoMatch());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Find persons by address, match")
    void findPersonsByAddressMatchTest() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByAddress(EntityDataTest.getPerson1And3AddressIgnoreCaseMatch());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Can't find persons by address, no match")
    void findPersonsByAddressMatchFailTest() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByAddress(EntityDataTest.getPersonAddressNoMatch());

        // then
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("Find persons by lastname, match")
    void findPersonsByLastNameMatch() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByLastName(EntityDataTest.getPersonLastNameIgnoreCaseMatch());
        
        // then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Can't Find persons by lastname, no match")
    void findPersonsByLastNameMatchFail() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByLastName(EntityDataTest.getPersonLastNameNoMatch());
        
        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Find persons by city, match")
    void findPersonsByCityNameMatch() {
        // given
        initModels();
        
        // when
        List<Person> result = personRepository.findByCity(EntityDataTest.getPersonCityIgnoreCaseMatch());
        
        // then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Can't Find persons by city, no match")
    void findPersonsByCityNoMatchFail() {
        // given
        initModels();

        // when
        List<Person> result = personRepository.findByLastName(EntityDataTest.getPersonCityNoMatch());

        // then
        assertThat(result).isEmpty();
    }
    
}
