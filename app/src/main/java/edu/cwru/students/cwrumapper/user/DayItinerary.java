package edu.cwru.students.cwrumapper.user;

import java.util.ArrayList;

public class DayItinerary {
    private ArrayList<Event> events;
    private int dayOfWeek;

    private Route routeInfo;
    private boolean isRouteUpdated;

    public DayItinerary(int dayOfWeek) {
        events = new ArrayList<>();
        this.dayOfWeek = dayOfWeek;

        routeInfo = null;
        isRouteUpdated = false;
    }

    public boolean addEvent() {
        if (isValid()) {

        } else {
            return false;
        }
        return false;
    }

    public boolean deleteEvent() {

        return false;
    }

    public boolean isValid() {

        return false;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Route getRouteInfo() {
        return routeInfo;
    }

    public void updateRouteInfo(Route newRouteInfo) {
        routeInfo = newRouteInfo;
        isRouteUpdated = true;
    }
}
