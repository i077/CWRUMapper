package edu.cwru.students.cwrumapper.user;

public class Event {
    private Location location;
    private double length;
    private int roomNumber;
    private int hour;
    private int min;
    private int sec;

    public Event(Location location, double length, int roomNumber, int hour, int min, int sec)
    {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public void editEvent(Location location, double length, int roomNumber, int hour, int min, int sec)
    {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public double getHour()
    {
        return hour;
    }

    public double getMin()
    {
        return min;
    }

    public double getSec()
    {
        return sec;
    }

    public Location getLocation()
    {
        return location;
    }

    public double getEventLength()
    {
        return length;
    }

    public double getRoomNumber()
    {
        return roomNumber;
    }

}
