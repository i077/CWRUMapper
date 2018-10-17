package edu.cwru.students.cwrumapper.user;
import java.util.ArrayList;
import java.util.Date;

public class User {

    private double id;
    private String name;
    private boolean student;
    private ArrayList<Itinerary> itineraries;

    //Used for creating Users for first time
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

    public User(Date startDate, double lengthOfStay) {

    }
    public User(double id, String name, boolean student)

}
