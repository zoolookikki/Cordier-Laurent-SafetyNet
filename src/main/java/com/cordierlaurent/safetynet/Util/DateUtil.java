package com.cordierlaurent.safetynet.Util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    
    public static int CalculateAge(String birthdate) {

        // Retourne -1 pour une date invalide
        if (birthdate == null || birthdate.isBlank()) return -1;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // whoaaa, MM en majuscule pour Mois et mm en minuscule pour minute...
            LocalDate start = LocalDate.parse(birthdate, formatter);
            LocalDate end = LocalDate.now();
            Period period = Period.between(start, end);
            return period.getYears();
        } catch (Exception e) {
            return -1;  // Retourne -1 en cas de format incorrect
        }
    }
    


}
