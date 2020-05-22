package com.example.sqlitedb;

public class PrimeNr {
    private Long primeNr;
    private String foundOn;

    // Constructor for primeNr, takes the prime number and the date it was found in string form.
    public PrimeNr(Long primeNr, String date) {
        this.primeNr = primeNr;
        this.foundOn = date;
    }

    /* Getters */

    public Long getPrimeNr() {
        return primeNr;
    }

    public String getFoundOn() {
        return foundOn;
    }
}
