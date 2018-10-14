package edu.cwru.students.cwrumapper.user;
import java.util.ArrayList;
import java.util.Date;

public class Itinerary {
    private boolean student;
    //type of user, if true, user is a student, if false, user is a guest
    private ArrayList<DayItinerary> ItinerariesForDays;
    private Date startDate;
    priavte int lengthOfStay;


    public Itinerary(boolean student, Date startDate, int lengthOfStay)
    {
        this.student = student;
        if(student)
        {
            ItinerariesForDays = new ArrayList<DayItinerary>(7);
            for(int i = 1; i<=8; i++)
            {
                DayItinerary filler = new DayItinerary(i);
                ItinerariesForDays.add(filler);
            }

        }
        else
        {

        }
    }

    public boolean addDay()
    {

    }
    public boolean editStartDay()
    {

    }
    public boolean editDay(int dayNumber)
    {

    }
}
