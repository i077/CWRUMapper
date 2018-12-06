package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * A class for holding the latitude and longitude coordinates for a particular location.
 * Also contains the name of the location.
 */
@Entity
public class Location implements Parcelable {
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

    public Location(Parcel in) {
        this.name = Objects.requireNonNull(in.readString());
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
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

    /**
     * Flatten Location data to a Parcel, to be bundled with an Event
     * @param dest The Parcel to write to
     * @param flags Flags to modify write behavior (not used here)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    /**
     * Out of scope for this project.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    public static final Parcelable.Creator<Location> CREATOR
            = new Parcelable.Creator<Location>() {

        /**
         * Create a Location from a given Parcel.
         * @param source Parcel to read from
         * @return A new Location containing data from {@param source}
         */
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
