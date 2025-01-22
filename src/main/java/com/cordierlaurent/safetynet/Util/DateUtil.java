package com.cordierlaurent.safetynet.Util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date-related operations.
 *
 */
public class DateUtil {
    
    // Private constructor to prevent instantiation of this utility class.
    private DateUtil() {
    }
    
    /**
     * Calculates the age based on the given birthdate.
     *
     * @param birthdate The birthdate as a string in the format MM/dd/yyyy.
     * @return The calculated age in years, or -1 if the input is invalid or the format is incorrect.
     */
    public static int calculateAge(String birthdate) {

        // Returns -1 for an invalid date.
        if (birthdate == null || birthdate.isBlank()) return -1;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Warning : MM = Month, mm = minutes.
            LocalDate start = LocalDate.parse(birthdate, formatter);
            LocalDate end = LocalDate.now();
            Period period = Period.between(start, end);
            return period.getYears();
        } catch (Exception e) {
            return -1;  // Returns -1 for an incorrect format or parsing error.
        }
    }

}
