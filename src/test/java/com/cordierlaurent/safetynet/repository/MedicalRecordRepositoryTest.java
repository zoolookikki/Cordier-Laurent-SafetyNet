package com.cordierlaurent.safetynet.repository;

import com.cordierlaurent.safetynet.Util.EntityDataTest;
import com.cordierlaurent.safetynet.model.MedicalRecord;

//import com.cordierlaurent.safetynet.model.MedicalRecord;

public class MedicalRecordRepositoryTest extends CrudRepositoryTest<MedicalRecord> {

    @Override
    protected void init() {
        repository = new MedicalRecordRepository();
        model1 = EntityDataTest.createMedicalRecord1();
        model1Updated = EntityDataTest.createMedicalRecord1Updated();
        id1UpdatedExist = EntityDataTest.createMedicalRecordId1UpdatedExist();
        id1UpdatedNotExist = EntityDataTest.createMedicalRecordId1UpdatedNotExist();
        model2 = EntityDataTest.createMedicalRecord2();
        id2 = EntityDataTest.createMedicalRecordId2();
        model3 = EntityDataTest.createMedicalRecord3();
    }
    
}
