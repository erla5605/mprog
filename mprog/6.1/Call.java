package com.mprog.ringaochtaemotsamtal;

import java.util.Date;

public class Call {

    private String name;
    private String number;
    private Date date;

    // Constructor for call.
    public Call(String name, String number, Date date) {
        this.name = name;
        this.number = number;
        this.date = date;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

}
