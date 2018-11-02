package edu.cwru.students.cwrumapper;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.*;

import static org.junit.Assert.*;

/**
 * Test functions of day itinerary, event and location
 */
public class DayItineraryUnitTest {

    DayItinerary temp;

    @BeforeClass
    public static void setup(){
    }

    @Test
    public void eventsCreated() {
        //check constructor
        temp = new DayItinerary();

        ArrayList<Event> events = temp.getEvents();
        assertNotNull(events);
    }

    @Test
    public void testAddEventCorrect(){
        //test for simple adding of event
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson",41.504099,-81.609537 );
        temp.addEvent("Lunch",tomlinson,36000,"B100" ,12,0,0);
        assertEquals(1,temp.getEvents().size());
    }

    @Test
    public void testDddEventIncorrectTime(){
        //test for adding of event with invalid time
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson",41.504099,-81.609537 );
        temp.addEvent("Lunch",tomlinson,36000,"B100" ,25,1000,0);
        assertEquals(0,temp.getEvents().size());
    }

    @Test
    public void testItineraryManipulation(){
        temp = new DayItinerary();
        Location tomlinson = new Location("Tomlinson",41.504099,-81.609537 );
        Location strosacker = new Location("Strosacker",41.503236, -81.607529 );
        Location veale = new Location("Veale",41.501090, -81.606373);
        temp.addEvent("Lunch",tomlinson,3600,"B100" ,12,0,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,3600,"B100" ,12,30,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,48000,"B100" ,13,30,0);
        assertEquals(1,temp.getEvents().size());

        temp.addEvent("Physics",strosacker,3600,"Auditorium" ,13,30,0);
        assertEquals(2,temp.getEvents().size());

        //PE will be added properly between Lunch and
        temp.addEvent("PE",veale,1800,"Gym" ,13,0,0);
        assertEquals(3,temp.getEvents().size());

        //This makes sure that physics was sorted properly to the middle of the itinerary
        assertEquals("PE", temp.getEvent(1).getName());

        temp.deleteEvent(0);
        assertEquals("PE", temp.getEvent(0).getName());

        temp.addEvent("Lunch",tomlinson,3600,"B100" ,12,0,0);
        assertEquals("Lunch", temp.getEvent(0).getName());

        temp.editEvent("LunchNew",0,tomlinson,3600,"B100" ,12,30,0);
        assertEquals(3,temp.getEvents().size());
        assertEquals("Lunch", temp.getEvent(0).getName());

        temp.editEvent("LunchNew",0,tomlinson,3600,"B100" ,15,30,0);
        assertEquals(3,temp.getEvents().size());
        assertEquals("LunchNew", temp.getEvent(2).getName());

    }


}