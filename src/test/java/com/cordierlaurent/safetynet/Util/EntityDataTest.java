package com.cordierlaurent.safetynet.Util;

import com.cordierlaurent.safetynet.model.Person;

public class EntityDataTest {

    public static Person createPerson1() {
        return new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
    }

    public static Person createPerson1Updated() {
        return new Person("John", "Boyd", "1510 Culver St", "Culver", "97452", "841-874-6513", "jaboyd@gmail.com");
    }

    public static Person createPerson2() {
        return new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
    }

    public static Person createPerson3() {
        return new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
    }

}

