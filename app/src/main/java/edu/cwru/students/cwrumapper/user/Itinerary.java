package edu.cwru.students.cwrumapper.user;
import java.util.ArrayList;
import java.util.Date;

public class Itinerary {
    private boolean student;
    //type of user, if true, user is a student, if false, user is a guest
    private ArrayList<DayItinerary> ItinerariesForDays;
    private Date startDate;
    private int lengthOfStay;

    public Itinerary() {
        student = true;
        startDate = new Date();
        ItinerariesForDays = new ArrayList<DayItinerary>(7);
        for(int i = 1; i<=7; i++) {
            DayItinerary filler = new DayItinerary(i);
            ItinerariesForDays.add(filler);
        }
    }

    public Itinerary(Date startDate, int lengthOfStay) {
        student = false;
        this.startDate = startDate;
        this.lengthOfStay = lengthOfStay;
        ItinerariesForDays = new ArrayList<DayItinerary>(lengthOfStay);
        for(int i = 1; i<=lengthOfStay; i++) {
            DayItinerary filler = new DayItinerary(i);
            ItinerariesForDays.add(filler);
        }
    }

    public boolean addDay() {
        //placeholder
        return true;
    }
    public boolean editStartDay() {
        //placeholder
        return true;
    }
    public boolean editDay(int dayNumber) {
        //placeholder
        return true;
    }
}
