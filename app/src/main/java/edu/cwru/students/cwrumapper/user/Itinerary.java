package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Embedded;

import java.util.ArrayList;
import java.util.Calendar;

public class Itinerary {
    private boolean student;
    //type of user, if true, user is a student, if false, user is a guest

    @Embedded
    private ArrayList<DayItinerary> itinerariesForDays;
    private Calendar startDate;
    private int lengthOfStay;

    public Itinerary() {
        student = true;
        startDate = Calendar.getInstance();
        lengthOfStay = 0;
        itinerariesForDays = new ArrayList<DayItinerary>(7);
        for(int i = 1; i<=7; i++) {
            DayItinerary filler = new DayItinerary();
            itinerariesForDays.add(filler);
        }
    }

    public Itinerary(Calendar startDate, int lengthOfStay) {
        student = false;
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

    public boolean addDay() {
        if (lengthOfStay > 6 || lengthOfStay == 0){
            return false;
        }
        DayItinerary filler = new DayItinerary();
        itinerariesForDays.add(filler);
        lengthOfStay++;
        return true;
    }


    public boolean editStartDay(Calendar newStartDate) {
        this.startDate = newStartDate;
        return true;
    }

    public boolean deleteDay() {
        if(lengthOfStay < 2){
            return false;
        }
        itinerariesForDays.remove(itinerariesForDays.size()-1);
        return true;
    }

    public int getLengthOfStay() {
        return lengthOfStay;
    }

    public Calendar getStartDate(){
        return startDate;
    }

    public ArrayList<DayItinerary> getItinerariesForDays(){
        return itinerariesForDays;
    }
}
