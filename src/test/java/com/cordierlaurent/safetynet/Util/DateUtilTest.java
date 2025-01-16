package com.cordierlaurent.safetynet.Util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class DateUtilTest {
    @Test
    void ValidDateTest() {
        // cela doit faire 0 tant que l'on a pas atteint le 01/01/2026 sinon ensuite cela fera + etc...
        // je préfère faire de cette façon pour ne pas avoir la même logique dans la fonction de test que dans la fonction à tester.
        String validDate = "01/01/2025"; 
        int expectedAge = LocalDate.now().getYear() - 2025; 
        assertEquals(expectedAge, DateUtil.calculateAge(validDate));
    }

    @Test
    void NullDateTest() {
        assertEquals(-1, DateUtil.calculateAge(null));
    }

    @Test
    void BlankDateTest() {
        assertEquals(-1, DateUtil.calculateAge(""));
    }

    @Test
    void InvalidFormatDateTest() {
        assertEquals(-1, DateUtil.calculateAge("31/12/2024"));
    }

    @Test
    void InvalidStringDateTest() {
        assertEquals(-1, DateUtil.calculateAge("xxxxxxx"));
    }
}
