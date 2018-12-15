package edu.cwru.students.cwrumapper;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.*;

import static org.junit.Assert.*;

/**
 * Test functions of User and Itinerary Classes
 */
public class UserUnitTest {

    private User user;
    private Calendar cal;
    private Location tomlinson;

    @Before
    public void startUp(){
        cal = Calendar.getInstance();

        tomlinson = new Location("Tomlinson", new LatLng[]{
                new LatLng(41.504188, -81.609537)});
    }

    @Test
    public void testConstructors(){
        user = new User(0,"Guest",new ArrayList<Itinerary>(), false);
        user = new User(13,"Amrish",new ArrayList<Itinerary>(), true);
        user = new User(10,"Amrish");
        user = new User(cal,4);
    }

    @Test
    public void testStudentCases(){
        user = new User(1,"Amrish");
        //constructor testing for student

        assertEquals("Amrish", user.getName());
        Location taft = new Location("Taft", new LatLng[]{
                new LatLng(41.512756, -81.607186)});
        user.addEvent(0,"Jolly", taft, 100, "100", 9, 0, 0);
        assertEquals(1,user.getId());
        assertTrue(user.isStudent());
        assertEquals(7,user.getItineraries().get(0).getItinerariesForDays().size());
        assertEquals("Jolly", user.getEvents(0).get(0).getName());



        user.setName("Imran");
        String name = user.getName();
        user.setId(15);
        int id = user.getId();
        boolean student = user.isStudent();
        ArrayList<Itinerary> itineraries = user.getItineraries();
        boolean newI = user.newItinerary();
        assertTrue(newI);
        newI = user.newItinerary(cal, 5);
        assertFalse(newI);
        boolean addE = user.addEvent(1,"Lunch",tomlinson,60,"B100" ,15,30,0);
        boolean addE2 = user.addEvent(1,"Dinner",tomlinson,60,"B100" ,12,30,0);
        boolean editE = user.editEvent(1,"DinnerNew",0,tomlinson,60,"B100" ,12,30,0);
        boolean add = user.addDay();
        assertFalse(add);
        boolean delete = user.deleteDay();
        assertFalse(delete);
        cal.set(Calendar.YEAR,2019);
        user.editStartDay(cal);
        boolean deleteE = user.deleteEvent(2,5);
        ArrayList<Event> eventsM = user.getEvents(1);
        ArrayList<Event> eventsT = user.getTodayEvents();
        ArrayList<Location> route = user.getRoute();
        user.setNewItineraryAsDefault(1);

        assertTrue(addE);
        assertTrue(addE2);
        assertTrue(editE);


    }

    @Test
    public void testGuestCases(){
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018,11,1);
        user = new User(startDate,8);
        //constructor testing for user

        assertEquals("Guest", user.getName());
        assertEquals(0,user.getId());
        assertFalse(user.isStudent());
        assertEquals(7,user.getItineraries().get(0).getItinerariesForDays().size());


        boolean student = user.isStudent();
        ArrayList<Itinerary> itineraries = user.getItineraries();
        boolean newI = user.newItinerary();
        assertFalse(newI);
        user.setItineraries(itineraries);
        newI = user.newItinerary(cal, 5);
        assertTrue(newI);
        boolean addE = user.addEvent(1,"Lunch",tomlinson,60,"B100" ,15,30,0);
        boolean addE2 = user.addEvent(1,"Dinner",tomlinson,60,"B100" ,12,30,0);
        boolean editE = user.editEvent(1,"DinnerNew",0,tomlinson,60,"B100" ,12,30,0);
        boolean add = user.addDay();
        assertTrue(add);
        boolean delete = user.deleteDay();
        assertTrue(delete);
        cal.set(Calendar.YEAR,2017);
        user.editStartDay(cal);
        boolean deleteE = user.deleteEvent(2,5);
        ArrayList<Event> eventsM = user.getEvents(1);
        ArrayList<Event> eventsT = user.getTodayEvents();
        ArrayList<Location> route = user.getRoute();
        user.setNewItineraryAsDefault(1);

        assertTrue(addE);
        assertTrue(addE2);
        assertTrue(editE);

    }


}