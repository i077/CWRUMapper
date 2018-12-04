package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class contains a list of day itineraries. It also contains the startDate and length
 * of stay if the user who has this class is a guest. It also contains student to check
 * if the user is a student or not.
 */
@Entity
public class Itinerary {
    private int userID;
    //type of user, if true, user is a student, if false, user is a guest

    @PrimaryKey
    private int id;
    @Ignore
    private ArrayList<DayItinerary> itinerariesForDays;
    @NonNull
    private Calendar startDate;
    private int lengthOfStay;

    /**
     * This is the itinerary constructor used for students. No params are required, and
     * student is set to true, while length of Stay is set to 0. The start date is set
     * arbitrarily to day this method is called. Blank day itineraries are also created
     * to fill the itinerary. For a student the itinerary is a weekly one, with each day
     * itinerary corresponding to a day of the week. Therefore, the start date and length are
     * immutable.
     */
    public Itinerary() {
        startDate = Calendar.getInstance();
        lengthOfStay = 0;
        itinerariesForDays = new ArrayList<DayItinerary>(7);
        for(int i = 1; i<=7; i++) {
            DayItinerary filler = new DayItinerary();
            itinerariesForDays.add(filler);
        }
    }

    /**
     * This is the itinerary constructor used for guests. Student is set to false.
     * @param startDate the startDate is used to set the startDate
     * @param lengthOfStay this is used to determine the amount of day itineraries needed
     * a max of 7 days can be added.
     */
    @Ignore
    public Itinerary(Calendar startDate, int lengthOfStay) {
        this.startDate = startDate;
        if (lengthOfStay > 7){
            lengthOfStay = 7;
        }
        this.lengthOfStay = lengthOfStay;
        itinerariesForDays = new ArrayList<DayItinerary>();
        for(int i = 1; i<=lengthOfStay; i++) {
            DayItinerary filler = new DayItinerary();
            itinerariesForDays.add(filler);
        }
    }

    public Itinerary(int id, int userID, ArrayList<DayItinerary> dayItineraries, Calendar cal, int lengthOfStay) {
        this.id = id;
        this.userID = userID;
        this.itinerariesForDays = dayItineraries;
        this.startDate = cal;
        this.lengthOfStay = lengthOfStay;
    }
    /**
     * A method used to add a new day to the itinerary, only works for guests, and only
     * if there are less than 7 days in the itinerary.
     * @return returns true if the the day itinerary has been added successfully, false if not
     */
    public boolean addDay() {
        if (lengthOfStay > 6 || lengthOfStay == 0){
            return false;
        }
        DayItinerary filler = new DayItinerary();
        itinerariesForDays.add(filler);
        lengthOfStay++;
        return true;
    }
    /**
     * Allows the user the change the start date to the one requested
     * @param newStartDate the new start date for the itinerary
     */
    public void editStartDay(Calendar newStartDate) {
        this.startDate = newStartDate;
    }
  
    /**
     * Allows the user to delete a day from teh itinerary as long as teh user is a guest
     * and has more than 1 days in his itinerary currently
     * @return returns true if the method was able to delete a day successfully
     */
    public boolean deleteDay() {
        if(lengthOfStay < 2){
            return false;
        }
        itinerariesForDays.remove(itinerariesForDays.size()-1);
        lengthOfStay--;
        return true;
    }

    /**
     * Getter method for obtaining the length of stay
     * @return returns the length of stay
     */
    public int getLengthOfStay() {
        return lengthOfStay;
    }

    /**
     * Getter method for the start date.
     * @return returns the start date
     */
    public Calendar getStartDate(){
        return startDate;
    }

    /**
     * Getter method for the list of day itineraries
     * @return returns the list of day itineraries
     */
    public ArrayList<DayItinerary> getItinerariesForDays(){
        return itinerariesForDays;
    }

    public void setItinerariesForDays(List<DayItinerary> itinerariesForDays) {this.itinerariesForDays = new ArrayList<>(itinerariesForDays);}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getUserID() {return userID;}

    public void setUserID(int id) {this.userID = id;}

    public void setLengthOfStay(int length) {this.lengthOfStay = length;}

    public void setStartDate(Calendar cal){this.startDate = cal;}


}
