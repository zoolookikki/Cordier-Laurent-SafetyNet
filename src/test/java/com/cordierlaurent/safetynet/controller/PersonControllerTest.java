package com.cordierlaurent.safetynet.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;
import com.cordierlaurent.safetynet.service.PersonService;

// pour ne charger que la partie concernant le MVC et précision de la classe pour ne charger que ce contrôleurs pour les tests.
@WebMvcTest(PersonController.class)
//public class PersonControllerTest {
public class PersonControllerTest  extends CrudControllerTest<Person> {

//    @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private JsonDataRepository jsonDataRepository;

    private static Person person1;
    private static Person person1Updated;

    @Override
    protected String getBaseUrl() {
        return "/person";
    }

    @Override
    protected Person getValidModel() {
        return person1;
    }

    @Override
    protected Person getUpdatedModel() {
        return person1Updated;
    }

    @Override
    protected CrudService<Person> getMockService() {
        return personService;
    }
    
    @Override
    protected String[] getIdForUpdate() {
        return new String[]{person1.getFirstName(), person1.getLastName()};
    }    
    
    @BeforeAll
    private static void setUp() {
        person1 = EntityDataTest.createPerson1();
        person1Updated = EntityDataTest.createPerson1Updated();
    }
    
}
