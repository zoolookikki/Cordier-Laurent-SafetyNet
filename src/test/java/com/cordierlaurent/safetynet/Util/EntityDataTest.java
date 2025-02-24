package com.cordierlaurent.safetynet.Util;

import java.util.List;
import java.util.Set;

import com.cordierlaurent.safetynet.model.FireStation;
import com.cordierlaurent.safetynet.model.MedicalRecord;
import com.cordierlaurent.safetynet.model.Person;

public class EntityDataTest {

    // Constructeur privé pour empêcher l'instanciation
    private EntityDataTest() {
    }
    
    public static Person createPerson1() {
        return new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    }

    public static Person createPerson2() {
        return new Person("Jacob", "Boyd", "9999 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
    }

    public static Person createPerson3() {
        return new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
    }

    public static Person createPerson4() {
        return new Person("Jonanathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "drk@email.com");
    }

    public static Person createPerson1Updated() {
        return new Person("John", "Boyd", "1510 Culver St", "Culver", "97452", "841-874-6513", "jaboyd@gmail.com");
    }


    public static String[] createPersonId1UpdatedExist() {
       return new String[]{"John", "Boyd"};
    }

    public static String[] createPersonId1UpdatedNotExist() {
        return new String[]{"xxxxx", "Boyd"};
    }
    
    public static String[] createPersonId2() {
        return new String[]{"Jacob", "Boyd"};
    }
    
    public static String getLastNameExist() {
        return "BOYD";
    }
    
    public static Person getPersonInvalidData() {
        return new Person("", "", "", "", "", "", "");
    }

    // préférable de toujours tester avec un IgnoreCase.
    public static String getPerson1And3AddressIgnoreCaseMatch() {
        return "1509 culver St";
    }

    public static String getPersonAddressNoMatch() {
        return "xxxx";
    }

    // préférable de toujours tester avec un IgnoreCase.
    public static String getPersonLastNameIgnoreCaseMatch() {
        return "BOYD";
    }

    public static String getPersonLastNameNoMatch() {
        return "XXXX";
    }

    // préférable de toujours tester avec un IgnoreCase.
    public static String getPersonCityIgnoreCaseMatch() {
        return "CULVER";
    }

    public static String getPersonCityNoMatch() {
        return "XXXX";
    }

    public static Set<String> getPersonAllAddressesMatch() {
        return Set.of("1509 Culver St", "1510 Culver St", "1511 Culver St");
    }

    public static Set<String> getPersonAllAddressesNoMatch() {
        return Set.of("xxxx", "yyyy", "zzzz");
    }

    public static MedicalRecord createMedicalRecord1() {
        return new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1984",
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );
    }

    public static MedicalRecord createMedicalRecord2() {
        return new MedicalRecord(
                "Jacob",
                "Boyd",
                "03/06/1989",
                List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                List.of("")
        );
    }

    public static MedicalRecord createMedicalRecord3() {
        return new MedicalRecord(
                "Tenley",
                "Boyd",
                "09/06/2017",
                List.of(""),
                List.of("")
        );
    }

    public static MedicalRecord createMedicalRecord1Updated() {
        return new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1989",
                List.of("aznol:1000mg", "hydrapermazol:10000mg"),
                List.of("xxxx", "yyyy", "zzzz")
        );
    }
    
    public static MedicalRecord getInvalidAgeMedicalRecord() {
        return new MedicalRecord(
                "John",
                "Boyd",
                "03/99/1989",
                List.of("aznol:1000mg", "hydrapermazol:10000mg"),
                List.of("xxxx", "yyyy", "zzzz")
        );
    }

    public static String[] createMedicalRecordId1UpdatedExist() {
       return new String[]{"John", "Boyd"};
    }

    public static String[] createMedicalRecordId1UpdatedNotExist() {
        return new String[]{"xxxxx", "Boyd"};
    }
    
    public static String[] createMedicalRecordId2() {
        return new String[]{"Jacob", "Boyd"};
    }

    public static FireStation createFireStation1() {
        return new FireStation("1509 Culver St", 3);
    }

    public static FireStation createFireStation2() {
        return new FireStation("29 15th St", 2);
    }

    public static FireStation createFireStation3() {
        return new FireStation("834 Binoc Ave", 3);
    }

    public static FireStation createFireStation1Updated() {
        return new FireStation("1509 Culver St", 9999);
    }

    public static String[] createFireStationId1UpdatedExist() {
       return new String[]{"1509 Culver St", "3"};
    }

    public static String[] createFireStationId1UpdatedNotExist() {
        return new String[]{"xxxxx", "999"};
    }
    
    public static String[] createFireStationId2() {
        return new String[]{"29 15th St", "2"};
    }
    
    public static int getStation3Exist() {
        return 3;
     }

    public static int getStationNotExist() {
        return 1234;
    }
    
    public static int getStation0Invalid() {
        return 0;
    }

}

