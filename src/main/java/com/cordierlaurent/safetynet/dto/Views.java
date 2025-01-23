package com.cordierlaurent.safetynet.dto;

/**
 * Defines various JSON views for controlling the serialization of DTOs.
 *
 * <p>Serialization levels:
 * <ul>
 *     <li>Basic : Includes only basic details such as first name, last name.</li>
 *     <li>Address : Extends Basic to include the address between lastName and phone.</li>
 *     <li>Detailed : Extends Basic without the address but nclude additional details such as age, medications, and allergies.</li>
 *     <li>WithEmail : A standalone view that includes specific details such as first name, last name, address, age, email, medications, and allergies.</li>
 * </ul>
 * </p>
 */
public class Views {
    public static class Basic {}
    public static class Address extends Basic {}
    public static class Detailed extends Basic {} 
    public static class WithEmail {} 
}
