package com.cordierlaurent.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cordierlaurent.safetynet.Util.DateUtil;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;
import com.cordierlaurent.safetynet.repository.CrudRepository;
import com.cordierlaurent.safetynet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService extends CrudService<MedicalRecord> {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    

    @Override
    protected boolean isSameModel(MedicalRecord model, MedicalRecord modelToVerify) {
        return (model.getFirstName().equalsIgnoreCase(modelToVerify.getFirstName()) &&
                model.getLastName().equalsIgnoreCase(modelToVerify.getLastName()));
            
    }

    @Override
    protected CrudRepository<MedicalRecord> getRepository() {
        return medicalRecordRepository;
    }
    
    public int age(Person person) {
        // on retrouve sa date de naissance via la clef unique dans MedicalRecord.
        String birthdate = medicalRecordRepository.findBirthdateByUniqueKey(
                new String[]{
                        person.getFirstName(), 
                        person.getLastName()});
        return DateUtil.CalculateAge(birthdate);        
    }

}
