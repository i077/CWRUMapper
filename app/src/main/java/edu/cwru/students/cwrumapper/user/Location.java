package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * A class for holding the latitude and longitude coordinates for a particular location.
 * Also contains the name of the location.
 */
@Entity
public class Location {
    @ColumnInfo(name = "LocationName")
    @PrimaryKey
    @NonNull
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

    public static Location[] populateData(){
        return new Location[]{
                new Location("Tinkham Veale North",41.508757, -81.608493),
                new Location("Tinkham Veale South	",41.507596, -81.608756),
                new Location("Taft",41.512771, -81.607163),
                new Location("Wyant",41.514214, -81.603250),
                new Location("Barnes & Noble",41.509962, -81.604270),
                new Location("Allen Ford",41.505825, -81.608632),
                new Location("Tomlinson",41.504188, -81.609537),
                new Location("Millis Schmitt",41.504099, -81.606873),
                new Location("Strosacker",41.503236, -81.607529),
                new Location("Veale",41.501090, -81.606373),
                new Location("Alumni",41.500547, -81.602553),
                new Location("Kusch",41.500787, -81.600249),
                new Location("PBL NE",41.510049, -81.607604),
                new Location("Wolstein",41.510653, -81.605950)

        };
    }
}
