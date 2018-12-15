package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

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
    private static final String TAG = "DayItinerary";

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
     * This method is called to edit an Event within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new Event is valid. If it is not
     * the Event will not be valid, and false would be returned.
     * @param name the new name of the even
     * @param index the index of the Event within the list
     * @param newLocation the new location of the Event
     * @param newLength the new length of the Event
     * @param newRoomNumber the new room number of the Event
     * @param newHour the new starting hour of the Event
     * @param newMin the new starting minute of the Event
     * @param newSec the new starting second of the Event
     * @return Returns true if the added Event is valid. If not no change occurs and false is returned.
     */
    public boolean editEvent(String name, int index, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(name, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        Event oldEvent = events.remove(index);
        if (index >= events.size() || index < 0) {
            return false;
        }

        //calls internal check to see if the new Event is valid
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
     * This method is called to edit an Event within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new Event is valid. If it is not
     * the Event will not be valid, and false would be returned.
     * @param name the new name of the even
     * @param newLocation the new location of the Event
     * @param newLength the new length of the Event
     * @param newRoomNumber the new room number of the Event
     * @param newHour the new starting hour of the Event
     * @param newMin the new starting minute of the Event
     * @param newSec the new starting second of the Event
     * @return Returns true if the added Event is valid. If not no change occurs and false is returned.
     */
    public boolean addEvent(String name, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        Event newEvent = new Event(name, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
        return addEvent(newEvent);
    }

    /**
     * Add an Event to the DayItinerary.
     * @param newEvent structure containing the Event to add
     * @return true if added Event is valid, and if not no change occurs
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
     * Method used to delete an Event, removes the Event from the array
     * @param index index of teh Event that is desired to be deleted
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
     * Remove an Event by ID.
     * Searches the list of events for a match
     * @param eventToRemove Event whose ID should match
     * @return True if successful, false if no matching event was found
     */
    public boolean deleteEvent(Event eventToRemove) {
       // Log.d(TAG, "Attempting to remove event " + eventToRemove.getId());
        for (Event e : events) {
            if (eventToRemove.getId() == e.getId()) {
                events.remove(e);
                return true;
            }
        }
     //   Log.w(TAG, "Failed to remove event " + eventToRemove.getId());
        return false;
    }

    /**
     * Checks to see if the Event is valid. It makes sure no conflicts exist between
     * the Event and any events stored in the array.
     * @param event the Event being checked against the current arraylist
     * @return returns false if the Event is not real or conflicts with an Event from
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
     * Getter method for retrieving a particular Event from the arraylist
     * @param index the index at which the desired Event is within the arraylist
     * @return returns the Event
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

    public static String intToWeekday(int weekdayNum) {
        switch (weekdayNum) {
            case 0: return "Monday";
            case 1: return "Tuesday";
            case 2: return "Wednesday";
            case 3: return "Thursday";
            case 4: return "Friday";
            case 5: return "Saturday";
            case 6: return "Sunday";
            default: return "";
        }
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
