package edu.cwru.students.cwrumapper.user;

public class Event {
    private Location location;
    private double length;
    private int roomNumber;
    private int hour;
    private int min;
    private int sec;
    private int endHour;
    private int endMin;
    private int endSec;

    public Event(Location location, int length, int roomNumber, int hour, int min, int sec) {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public void editEvent(Location location, double length, int roomNumber, int hour, int min, int sec) {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public int getEndHour() { return endHour; }

    public int getEndMin() { return endMin; }

    public int getEndSec() { return endSec; }

    public Location getLocation() { return location; }

    public double getEventLength() { return length; }

    public double getRoomNumber() { return roomNumber; }

}
