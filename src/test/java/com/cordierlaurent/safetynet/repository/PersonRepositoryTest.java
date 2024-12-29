package com.cordierlaurent.safetynet.repository;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.Person;

public class PersonRepositoryTest extends CrudRepositoryTest<Person>{

    @Override
    protected void init() {
        repository = new PersonRepository();
        model1 = EntityDataTest.createPerson1();
        model1Updated = EntityDataTest.createPerson1Updated();
        id1UpdatedExist = EntityDataTest.createPersonId1UpdatedExist();
        id1UpdatedNotExist = EntityDataTest.createPersonId1UpdatedNotExist();
        model2 = EntityDataTest.createPerson2();
        id2 = EntityDataTest.createPersonId2();
        model3 = EntityDataTest.createPerson3();
    }
    
}
