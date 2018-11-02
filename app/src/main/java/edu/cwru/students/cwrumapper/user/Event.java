package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Embedded;

/**
 * This class serves to be used as an object that holds a few key details that are
 * described by each event. It contains the name of the event, location, room number,
 * the length of the event, the start time, and the end time. Getter methods can be
 * called to retrieve information.
 */
public class Event implements Comparable<Event>{

    @Embedded
    private Location location;
    private int length;
    private String roomNumber;
    private int hour;
    private int min;
    private int sec;
    private int endHour;
    private int endMin;
    private int endSec;
    private String name;

    /**
     * The constructor which passes input values into field names
     * @param name The name of the event
     * @param location The location of the event
     * @param length The length of the event
     * @param roomNumber The room number
     * @param hour The starting hour
     * @param min The starting minute
     * @param sec The starting second
     */
    public Event(String name, Location location, int length, String roomNumber, int hour, int min, int sec) {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;

        //finds the end time
        this.endHour = hour + (sec+length)/3600;
        this.endMin = min + ((sec+length)%3600)/60;
        this.endSec = ((sec+length)%60);
        this.name = name;
    }

    /**
     * Used to implement the comparable interface. Used to compare two events by their
     * starting hour. If the events have the same time, the location is compared.
     * @param other Another event class being compared to
     * @return Return -1 when the starting time of this event is before the other event.
     * Will return 0 if the events are the same and have the same location. And will return
     * 1 if this event occurs after the other event.
     */
    public int compareTo(Event other){
        int hourT = Integer.compare(this.hour, other.getHour());
        if(hourT != 0){
            return hourT;
        } else{
            int minT = Integer.compare(this.hour, other.getMin());
            if(minT != 0) {
                return minT;
            } else {
                int secT = Integer.compare(this.hour, other.getSec());
                if(secT != 0) {
                    return secT;
                }else{
                    return this.location.getName().compareTo(other.getLocation().getName());
                }
            }
        }
    }

    /**
     * Checks to see if there is a conflict between the length of this event or the length
     * of the other event. Also checks if the event times are real.
     * @param other The other event being checked against.
     * @return If the start time of this event occurs between the start time and end time of the other event,
     * returns false. If the end time of this event occurs in between the start and end times
     * of the other event, returns false. Returns false if there is a the end or start time is
     * not real. Returns true if none of these cases occur.
     */
    public boolean isConflict(Event other){
        if(!(this.isRealTime() && other.isRealTime())){
            return true;
        }

        //finds the any conflicts with the timings
        int tempThisStartTime = this.hour*3600+this.min*60+this.sec;
        int tempOtherStartTime = other.getHour()*3600+other.getMin()*60+other.getSec();
        int tempThisEndTime = this.endHour*3600+this.endMin*60+this.endSec;
        int tempOtherEndTime = other.getEndHour()*3600+other.getEndMin()*60+other.getEndSec();
        return ((tempOtherEndTime > tempThisStartTime && tempOtherEndTime < tempThisEndTime)||(tempOtherStartTime > tempThisStartTime && tempOtherStartTime < tempThisEndTime));
    }

    /**
     * Checks if this time is real.
     * @return Returns true if the start time and end time are real (can't go past midnight)
     */
    public boolean isRealTime(){
        boolean condition1 =  length>0;
        boolean condition2 = -1 < hour && hour < 24 && endHour < 24;
        boolean condition3 = sec >= 0 &&  min >= 0 && sec < 60 && min < 60;
        return condition1 && condition2 && condition3;
    }

    /**
     * Getter method for hour
     * @return returns the starting hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Getter method fot the minute
     * @return returns the starting minute
     */
    public int getMin() {
        return min;
    }

    /**
     * Getter method for the second
     * @return returns the starting second
     */
    public int getSec() {
        return sec;
    }

    /**
     * Getter method for the ending hour
     * @return returns the hour in which the event ends
     */
    public int getEndHour() {
        return endHour;
    }
    /**
     * Getter method for the ending minute
     * @return returns the minute in which the event ends
     */
    public int getEndMin() {
        return endMin;
    }
    /**
     * Getter method for the ending second
     * @return returns the second in which the event ends
     */
    public int getEndSec() {
        return endSec;
    }
    /**
     * Getter method for the location
     * @return returns a location object for teh event's location
     */
    public Location getLocation() {
        return location;
    }
    /**
     * Getter method for the length of the event
     * @return returns the length of the even
     */
    public double getEventLength() {
        return length;
    }

    /**
     * Getter method for the room number
     * @return returns a String for the room number
     */
    public String getRoomNumber() {
        return roomNumber;
    }
    /**
     * Getter method for the name
     * @return returns the name of the event
     */
    public String getName() { return name; }

}
