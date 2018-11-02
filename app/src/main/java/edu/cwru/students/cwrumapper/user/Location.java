package edu.cwru.students.cwrumapper.user;

/**
 * A class for holding the latitude and longitude coordinates for a particular location.
 * Also contains the name of the location.
 */
public class Location {
    private String name;
    private double latitude;
    private double longitude;

    /**
     * The constructor for a location object, need to pass in the name, latitude, and longitude
     * @param name Name of the location to be stored
     * @param latitude Latitude coordinate of the location
     * @param longitude Longitude coordinate of the location
     */

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter method for the name
     * @return The name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the latitude coordinate
     * @return The latitude coordinate for the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter method for the longitude coordinate
     * @return The longitude coordinate for the location
     */
    public double getLongitude() {
        return longitude;
    }
}
