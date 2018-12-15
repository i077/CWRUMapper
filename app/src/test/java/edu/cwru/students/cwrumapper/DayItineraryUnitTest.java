package edu.cwru.students.cwrumapper;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.*;

import static org.junit.Assert.*;

/**
 * Test functions of day itinerary, event and location
 */
public class DayItineraryUnitTest {

    DayItinerary temp;

    @Before
    public void setup(){
        temp = new DayItinerary();

    }

    @Test
    public void eventsCreated() {
        ArrayList<Event> events = temp.getEvents();
        assertNotNull(events);
    }

    @Test
    public void testAddEventCorrect(){
        //test for simple adding of event
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson", new LatLng[]{
                new LatLng(41.504188, -81.609537)});
        temp.addEvent("Lunch",tomlinson,10,"B100" ,12,0,0);
        assertEquals(1,temp.getEvents().size());
    }

    @Test
    public void testDddEventIncorrectTime(){
        //test for adding of event with invalid time
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson", new LatLng[]{
                new LatLng(41.504188, -81.609537)});
        temp.addEvent("Lunch",tomlinson,10,"B100" ,25,1000,0);
        assertEquals(0,temp.getEvents().size());
    }

    @Test
    public void testItineraryManipulation(){
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson", new LatLng[]{
                new LatLng(41.504188, -81.609537)});
        Location strosacker = new Location("Strosacker", new LatLng[]{
                new LatLng(41.503236, -81.607529)});
        Location veale = new Location("Veale", new LatLng[]{
                new LatLng(41.501090, -81.606373)});
        temp.addEvent("Lunch",tomlinson,60,"B100" ,12,0,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,60,"B100" ,12,30,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,800,"B100" ,13,30,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,60,"Auditorium" ,13,30,0);
        assertEquals(2,temp.getEvents().size());

        //PE will be added properly between Lunch and
        temp.addEvent("PE",veale,30,"Gym" ,13,0,0);
        assertEquals(3,temp.getEvents().size());

        //This makes sure that physics was sorted properly to the middle of the itinerary
        assertEquals("PE", temp.getEvent(1).getName());

        temp.deleteEvent(0);
        assertEquals("PE", temp.getEvent(0).getName());

        temp.addEvent("Lunch",tomlinson,60,"B100" ,12,0,0);
        assertEquals("Lunch", temp.getEvent(0).getName());

        temp.editEvent("LunchNew",0,tomlinson,60,"B100" ,12,30,0);
        assertEquals(3,temp.getEvents().size());
        assertEquals("Lunch", temp.getEvent(0).getName());

        temp.editEvent("LunchNew",0,tomlinson,60,"B100" ,15,30,0);
        assertEquals(3,temp.getEvents().size());
        assertEquals("LunchNew", temp.getEvent(2).getName());

        Event event = new Event("LunchNew",tomlinson,60,"B100" ,20,30,0);
        temp.addEvent(event);
        temp.deleteEvent(event);
        String day = DayItinerary.intToWeekday(0);
        assertEquals(day, "Monday");
        day = DayItinerary.intToWeekday(1);
        assertEquals(day, "Tuesday");
        day = DayItinerary.intToWeekday(2);
        assertEquals(day, "Wednesday");
        day = DayItinerary.intToWeekday(3);
        assertEquals(day, "Thursday");
        day = DayItinerary.intToWeekday(4);
        assertEquals(day, "Friday");
        day = DayItinerary.intToWeekday(5);
        assertEquals(day, "Saturday");
        day = DayItinerary.intToWeekday(6);
        assertEquals(day, "Sunday");

        List<Event> events = new ArrayList<Event>();

        temp.setId(2);
        temp.setItineraryID(1);
        temp.setEvents(events);
        temp.setRouteUpdated(true);
        assertTrue(temp.getRouteUpdated());
        assertEquals(1, temp.getItineraryID());
        assertEquals(2, temp.getId());


        /*
        Route routeInfo = temp.getRouteInfo();

        boolean check = temp.getRouteUpdated();
        temp.updateRouteInfo(routeInfo);
        boolean check2 = temp.getRouteUpdated();

        ArrayList<Location> locations = temp.getLocation();
        temp.setId(1);
        temp.setItineraryID(2);
        int id = temp.getId();
        int itineraryID = temp.getItineraryID();
        ArrayList<Event> events = temp.getEvents();
        temp = new DayItinerary(1,1,events,routeInfo,false);

        assertEquals(1,id);
        assertEquals(2,itineraryID);
        assertNotNull(events);
        */


    }


}