package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.cwru.students.cwrumapper.Archived.ConverterItinerary;

/**
 * Most vital class for the backend. Each user has one of these objects. Can be a guest
 * or a student. Contains an id, which is the student id, or it is 0 for a guest.
 * The object also has a name, and has a check for whether the user is a student or guest.
 * The class also contains a list of itineraries with the most recent or active itinerary
 * having an index of 0. The class is also used to store into database, and is used
 * by the repository and UserDatabase.
 */
@Entity(tableName = "user_table")
@TypeConverters(ConverterItinerary.class)
public class User {

    @PrimaryKey
    private int id;
    private String name;
    public boolean student;
    @Ignore
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

    /**
     * The constructor that is used if the user is a guest and is signing in for the first time.
     * The user is required to enter a start date and specifiy a length of stay.
     * The values will be used to create a new itinerary. The id is set to 0, and the
     * name is set to Guest.
     * @param startDate the start date of the itinerary.
     * @param lengthOfStay the length of stay for the guest
     */
    @Ignore
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

    public User(int id, String name, ArrayList<Itinerary> itineraries, boolean student){
        this.id = id;
        this.name = name;
        this.itineraries = itineraries;
        this.student = student;
    }

    public void setName(String newName){name = newName;}

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
    public boolean newItinerary(){
        if(student) {
            Itinerary firstItin = new Itinerary();
            itineraries.add(0, firstItin);
            return true;
        }
        return false;
    }

    /**
     * Creates an itinerary for a guest. Creates an itineraary with the start date and length
     * of the visit.
     * @param startDate the start date of the visit
     * @param lengthOfStay the length of the visit
     */
    public boolean newItinerary(Calendar startDate, int lengthOfStay){
        if(!student) {
            Itinerary firstItin = new Itinerary(startDate,lengthOfStay);
            itineraries.add(0, firstItin);
            return true;
        }
        return false;
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

    @NonNull
    public ArrayList<Event> getTodayEvents() {
        Itinerary itinerary = itineraries.get(0);
        int day;
        if(student) {
            Calendar today = Calendar.getInstance();
            day = today.get(Calendar.DAY_OF_WEEK);
            return itinerary.getItinerariesForDays().get(day - 1).getEvents();
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
            return itinerary.getItinerariesForDays().get(day).getEvents();
        }
    }

    @NonNull
    public ArrayList<Event> getEvents(int day) {
        Itinerary itinerary = itineraries.get(0);
        if (!student && (day+1 > itineraries.get(0).getLengthOfStay() || day < 0)){
            return itinerary.getItinerariesForDays().get(0).getEvents();
        }
        if (student && (day > 6 || day < 0) ){
            return itinerary.getItinerariesForDays().get(0).getEvents();
        }
        return itinerary.getItinerariesForDays().get(day).getEvents();
    }
    /**
     * This method is called to edit an mEvent within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new mEvent is valid. If it is not
     * the mEvent will not be valid, and false would be returned.
     * @param day the day of the week or day of the mEvent that the itinerary is being modified for
     * @param name the new name of the even
     * @param index the index of the mEvent within the list
     * @param newLocation the new location of the mEvent
     * @param newLength the new length of the mEvent
     * @param newRoomNumber the new room number of the mEvent
     * @param newHour the new starting hour of the mEvent
     * @param newMin the new starting minute of the mEvent
     * @param newSec the new starting second of the mEvent
     * @return Returns true if the added mEvent is valid. If not no change occurs and false is returned.
     */
    public boolean editEvent(int day, String name, int index, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        if (!student && (day+1 > itineraries.get(0).getLengthOfStay() || day < 0)){
            return false;
        }
        if (student && (day > 6 || day < 0) ){
            return false;
        }

        return itineraries.get(0).getItinerariesForDays().get(day).editEvent(name, index, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
    }

    /**
     * This method is called to edit an mEvent within the itinerary, all params are necessary.
     * It also carries the important function of checking if the new mEvent is valid. If it is not
     * the mEvent will not be valid, and false would be returned.
     * @param day the day of the week or day of the mEvent that the itinerary is being modified for
     * @param name the new name of the even
     * @param newLocation the new location of the mEvent
     * @param newLength the new length of the mEvent
     * @param newRoomNumber the new room number of the mEvent
     * @param newHour the new starting hour of the mEvent
     * @param newMin the new starting minute of the mEvent
     * @param newSec the new starting second of the mEvent
     * @return Returns true if the added mEvent is valid. If not no change occurs and false is returned.
     */
    public boolean addEvent(int day, String name, Location newLocation, int newLength, String newRoomNumber, int newHour, int newMin, int newSec) {
        if (!student && (day+1 > itineraries.get(0).getLengthOfStay() || day < 0)){
            return false;
        }
        if (student && (day > 6 || day < 0) ){
            return false;
        }

        return itineraries.get(0).getItinerariesForDays().get(day).addEvent(name, newLocation, newLength, newRoomNumber, newHour, newMin, newSec);
    }

    /**
     * Method used to delte an mEvent, removes the mEvent from the array
     * @param day the day of the week or day of the mEvent that the itinerary is being modified for
     * @param index index of the mEvent that is desired to be deleted
     * @return Returns true if the deleted mEvent day and mEvent index is valid. If not no change occurs and false is returned.
     */
    public boolean deleteEvent(int day, int index) {
        if (!student && (day+1 > itineraries.get(0).getLengthOfStay() || day < 0)){
            return false;
        }
        if (student && (day > 6 || day < 0) ){
            return false;
        }

        return itineraries.get(0).getItinerariesForDays().get(day).deleteEvent(index);
    }


    /**
     * A method used to add a new day to the itinerary, only works for guests, and only
     * if there are less than 7 days in the itinerary.
     * @return returns true if the the day itinerary has been added successfully, false if not
     */
    public boolean addDay() {
        return itineraries.get(0).addDay();
    }
    /**
     * Allows the user the change the start date to the one requested
     * @param newStartDate the new start date for the itinerary
     */
    public void editStartDay(Calendar newStartDate) {
        itineraries.get(0).editStartDay(newStartDate);
    }

    /**
     * Allows the user to delete a day from teh itinerary as long as teh user is a guest
     * and has more than 1 days in his itinerary currently
     * @return returns true if the method was able to delete a day successfully
     */
    public boolean deleteDay() {
        return itineraries.get(0).deleteDay();
    }

    public void setId(int id) {this.id = id;}

    public void setItineraries(List<Itinerary> itineraries) {this.itineraries = new ArrayList<>(itineraries);}
}
