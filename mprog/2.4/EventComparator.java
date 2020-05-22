package com.example.anvandakalender;

import java.util.Comparator;

// Sorts the events based on start time.
public class EventComparator implements Comparator<Event> {

    // Compares two events based on their start time.
    @Override
    public int compare(Event event1, Event event2) {
        return event1.getStart().compareTo(event2.getStart());
    }
}
