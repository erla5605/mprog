package com.example.anvandakontakter;

public class Contact {

    private String name;
    private String phoneNr;

    // Constructor for contact takes name.
    public Contact(String name) {
        this(name, "Phone number missing");
    }

    // Constructor for contact takes name and phone number.
    public Contact(String name, String phoneNr){
        this.name = name;
        this.phoneNr = phoneNr;
    }

    /*Getters*/

    public String getName() {
        return name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    // To string for contact.
    public String toString(){
        return name + " " + phoneNr;
    }
}
