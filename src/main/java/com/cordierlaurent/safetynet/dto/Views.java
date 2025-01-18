package com.cordierlaurent.safetynet.dto;

public class Views {
    // firstName, lastName, phone.
    public static class Basic {}
    // with the additional address between lastName and phone : firstName, lastName, address, phone.
    public static class Address extends Basic {}
    // without the address but with additional details : firstName, lastName, phone, age, medications, allergies.
    public static class Detailed extends Basic {} 
    // impossible to extend because differences : firstName, lastName, address, age, email, medications, allergies.
    public static class WithEmail {} 
}
