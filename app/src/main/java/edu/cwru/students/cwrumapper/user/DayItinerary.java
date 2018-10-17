package edu.cwru.students.cwrumapper.user;

import java.util.ArrayList;
import java.util.Collections;

public class DayItinerary {
    private ArrayList<Event> events;

    public DayItinerary() {
        events = new ArrayList<Event>();
    }

    public boolean editEvent(int index, Location newLocation, int newLength, int newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        Event oldEvent = events.remove(index);
        if(isValid(newEvent)){
            events.add(index,newEvent);
            return true;
        } else {
            events.add(index,oldEvent);
            return false;
        }


    }

    public boolean addEvent(int index, Location newLocation, int newLength, int newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        if (isValid(newEvent)) {
            events.add(index,newEvent);
            Collections.sort(events);
            return true;
        } else {
            return false;
        }
    }

    public void deleteEvent(int index) {
        events.remove(index);
    }

    public boolean isValid(Event event) {
        for(Event temp : events) {
            if(event.isConflict(temp)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Event> getEvents(){
        return events;
    }

}
