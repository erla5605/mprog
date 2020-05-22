package com.example.anvandakalender;

import java.util.Date;

public class Event {

    private String title;
    private Date start;
    private Date end;

    // Constructor for Event.
    public Event(String title, Date start, Date end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    // Returns the title of the event.
    public String getTitle() {
        return title;
    }

    // Sets the title of the event.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the start time of the event.
    public Date getStart() {
        return start;
    }

    // Sets the start time of the event.
    public void setStart(Date start) {
        this.start = start;
    }

    // Returns the end time of the event.
    public Date getEnd() {
        return end;
    }

    // Sets the end time of the event.
    public void setEnd(Date end) {
        this.end = end;
    }
}
