package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an important class that holds a user's day itinerary. This is contains
 * a list of events for the day. This can then be used to create a route. Event manipulation
 * also takes place here.
 */
@Entity
public class DayItinerary {
    private int itineraryID;

    @PrimaryKey
    private int id;

    @Ignore
    private ArrayList<Event> events;

    @Ignore
    private Route routeInfo;
    private boolean isRouteUpdated;


    /**
     * This is the constructor, which just creates an array list of events, but does not fill this list
     */
    public DayItinerary() {
        events = new ArrayList<Event>();
        isRouteUpdated = false;
    }

    /**
     * This method is called to edit an mEvent within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new mEvent is valid. If it is not
     * the mEvent will not be valid, and false would be returned.
     * @param name the new name of the even
     * @param index the index of the mEvent within the list
     * @param newLocation the new location of the mEvent
     * @param newLength the new length of the mEvent
     * @param newRoomNumber the new room number of the mEvent
     * @param newHour the new starting hour of the mEvent
     * @param newMin the new starting minute of the mEvent
     * @param newSec the new starting second of the mEvent
     * @return Returns true if the added mEvent is valid. If not no change occurs and false is returned.
     */
    public boolean editEvent(String name, int index, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(name, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        Event oldEvent = events.remove(index);
        if (index >= events.size() || index < 0) {
            return false;
        }

        //calls internal check to see if the new mEvent is valid
        if(isValid(newEvent)){
            events.add(index,newEvent);
            Collections.sort(events);
            return true;
        } else {
            events.add(index,oldEvent);
            return false;
        }


    }
    /**
     * This method is called to edit an mEvent within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new mEvent is valid. If it is not
     * the mEvent will not be valid, and false would be returned.
     * @param name the new name of the even
     * @param newLocation the new location of the mEvent
     * @param newLength the new length of the mEvent
     * @param newRoomNumber the new room number of the mEvent
     * @param newHour the new starting hour of the mEvent
     * @param newMin the new starting minute of the mEvent
     * @param newSec the new starting second of the mEvent
     * @return Returns true if the added mEvent is valid. If not no change occurs and false is returned.
     */
    public boolean addEvent(String name, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(name, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        return addEvent(newEvent);
    }

    /**
     * Add an Event to the DayItinerary.
     * @param newEvent structure containing the mEvent to add
     * @return true if added mEvent is valid, and if not no change occurs
     */
    public boolean addEvent(Event newEvent) {
        if (isValid(newEvent)) {
            events.add(newEvent);

            //Since events is comparable, this list can be sorted after addition
            Collections.sort(events);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method used to delete an mEvent, removes the mEvent from the array
     * @param index index of teh mEvent that is desired to be deleted
     * @return Returns true if the index is valid, if not, false is returned
     */
    public boolean deleteEvent(int index) {
        if (index >= events.size() || index < 0) {
            return false;
        }
        events.remove(index);
        return true;
    }

    /**
     * Checks to see if the mEvent is valid. It makes sure no conflicts exist between
     * the mEvent and any events stored in the array.
     * @param event the mEvent being checked against the current arraylist
     * @return returns false if the mEvent is not real or conflicts with an mEvent from
     * the arraylist
     */
    private boolean isValid(Event event) {
        for(Event temp : events) {
            if(event.isConflict(temp)) {
                return false;
            }
        }
        if(!event.isRealTime()) {
            return false;
        }
        return true;
    }

    /**
     * Getter method for retreiving the arry list
     * @return returns the arraylist of events
     */
    public ArrayList<Event> getEvents(){
        return events;
    }

    /**
     * Getter method for retrieving a particular mEvent from the arraylist
     * @param index the index at which the desired mEvent is within the arraylist
     * @return returns the mEvent
     */
    public Event getEvent(int index) {return events.get(index);}

    /**
     * Used for getting a list of all the locations for the day
     * @return returns a list of all the locations for the day
     */
    public ArrayList<Location> getLocation() {
        ArrayList<Location> routeLocations = new ArrayList<>();
        for (Event event : events) {
            routeLocations.add(event.getLocation());
        }
        return routeLocations;
    }

    public boolean getRouteUpdated(){
        return isRouteUpdated;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getItineraryID() {return itineraryID;}

    public void setItineraryID(int id) {this.itineraryID = id;}

    public void setEvents(List<Event> events) {this.events = new ArrayList<>(events);}

    public void setRouteUpdated(boolean updated) {this.isRouteUpdated = updated;}

}
