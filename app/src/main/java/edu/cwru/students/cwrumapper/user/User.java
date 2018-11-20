package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Most vital class for the backend. Each user has one of these objects. Can be a guest
 * or a student. Contains an id, which is the student id, or it is 0 for a guest.
 * The object also has a name, and has a check for whether the user is a student or guest.
 * The class also contains a list of itineraries with the most recent or active itinerary
 * having an index of 0. The class is also used to store into database, and is used
 * by the repository and UserDatabase.
 */
@Entity
public class User {

    @PrimaryKey
    private int id;
    private String name;
    public boolean student;

    @Embedded
    public ArrayList<Itinerary> itineraries;

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

    /**
     * The constructor that is used if the user is a guest and is signing in for the first time.
     * The user is required to enter a start date and specifiy a length of stay.
     * The values will be used to create a new itinerary. The id is set to 0, and the
     * name is set to Guest.
     * @param startDate the start date of the itinerary.
     * @param lengthOfStay the length of stay for the guest
     */
    public User(Calendar startDate, int lengthOfStay) {
        itineraries = new ArrayList<>();
        itineraries.add(new Itinerary(startDate, lengthOfStay));
        id = 0;
        name = "Guest";
        student = false;
    }

    /**
     * This is the constructor used if the user is a student. The id and name are stored, and a
     * default weekly itinerary is created.
     * @param id The id of the student returned by the sign in process.
     * @param name the name of the student.
     */
    public User(int id, String name){
        itineraries = new ArrayList<>();
        itineraries.add(new Itinerary());
        this.id = id;
        this.name = name;
        this.student = true;

    }

    /**
     * A getter method to retrieve the student id
     * @return returns the id of the student
     */
    public int getId(){
        return id;
    }

    /**
     * Check to see if teh user is a student
     * @return returns true if the user is a student
     */
    public boolean isStudent() {
        return student;
    }

    /**
     * Getter method to retrieve the itineraries of the user
     * @return returns the list of itineraries
     */
    @NonNull
    public ArrayList<Itinerary> getItineraries() {
        return itineraries;
    }

    /**
     * Getter method to retrieve the user's name
     * @return returns the user's name
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Creates a new itinerary. Only works for students as it creates a default itienrary.
     * Sets the itinerary as active.
     */
    public void newItinerary(){
        if(student) {
            Itinerary firstItin = new Itinerary();
            itineraries.add(0, firstItin);
        }
    }

    /**
     * Creates an itinerary for a guest. Creates an itineraary with the start date and length
     * of the visit.
     * @param startDate the start date of the visit
     * @param lengthOfStay the length of the visit
     */
    public void newItinerary(Calendar startDate, int lengthOfStay){
        if(!student) {
            Itinerary firstItin = new Itinerary(startDate,lengthOfStay);
            itineraries.add(0, firstItin);
        }
    }

    /**
     * Sets the specified itinerary as the active one.
     * @param index the index of the desired itinerary
     */
    public void setNewItineraryAsDefault(int index){
        Itinerary temp = itineraries.remove(index);
        itineraries.add(0,temp);
    }

    /**
     * This is a really important method for creating the route. It is a wrapper that
     * returns the locations for all the events for the current day. This can be fed
     * into the Google API Map.
     * @return
     */
    @NonNull
    public ArrayList<Location> getRoute(){
        Itinerary itinerary = itineraries.get(0);
        int day;
        if(student) {
            Calendar today = Calendar.getInstance();
            day = today.get(Calendar.DAY_OF_WEEK);
            return itinerary.getItinerariesForDays().get(day - 1).getLocation();
        }
        else{
            Calendar today = Calendar.getInstance();
            if(today.after(itinerary.getStartDate())){
                int dayTemp = (today.get(Calendar.DAY_OF_WEEK)-itinerary.getStartDate().get(Calendar.DAY_OF_WEEK));
                if(dayTemp<0){
                    day = dayTemp +7;
                }
                else{
                    day = dayTemp;
                }
            }
            else{
                day = 0;
            }
            return itinerary.getItinerariesForDays().get(day).getLocation();
        }

    }
}
