package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Entity
public class User {

    @PrimaryKey
    private int id;
    private String name;
    private boolean student;

    @Embedded
    private ArrayList<Itinerary> itineraries;

    //Used for creating Users for first time
    /*
    public User(GoogleAuth details) {
        id = details.getID();
        name = details.getName();
        student = true;
        itineraries = new ArrayList<Itinerary>();
        Date tempDate = new Date();
        Itinerary firstItin = new Itinerary(1,tempDate);
        itineraries.add(firstItin);

        FileManager.Save();
    }
    */

    public User(Date startDate, double lengthOfStay) {

    }
    public User(double id, String name, boolean student){

    }

    public int getId(){
        return id;
    }

    public boolean isStudent() {
        return student;
    }

    @NonNull
    public ArrayList<Itinerary> getItineraries() {
        return itineraries;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void newItinerary(){
        if(student) {
            Itinerary firstItin = new Itinerary();
            itineraries.add(firstItin);
        }
    }

    public void newItinerary(Calendar cal, int lengthOfStay){
        if(!student) {
            Itinerary firstItin = new Itinerary(cal,lengthOfStay);
            itineraries.add(firstItin);
        }
    }

    public void setNewItineraryAsDefault(int index){
        Itinerary temp = itineraries.remove(index);
        itineraries.add(0,temp);
    }
}
