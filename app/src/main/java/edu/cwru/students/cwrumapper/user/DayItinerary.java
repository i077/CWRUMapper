package edu.cwru.students.cwrumapper.user;

import java.util.ArrayList;

public class DayItinerary {
    private ArrayList<Event> events;
    private int dayOfWeek;

    public DayItinerary(int dayOfWeek) {
        events = new ArrayList<Event>();
        this.dayOfWeek = dayOfWeek;
    }

    public boolean addEvent() {
        if (isValid()) {

        } else {
            return false;
        }

    }

    public boolean deleteEvent() {

    }

    public boolean isValid() {

    }

}
