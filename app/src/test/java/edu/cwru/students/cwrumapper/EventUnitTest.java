package edu.cwru.students.cwrumapper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.*;

import static org.junit.Assert.*;

/**
 * Test functions of Event Class
 */
public class EventUnitTest {

    private Event e1;
    private Event e2;
    private Event e3;
    private Event e4;
    private Location tomlinson;

    @Before
    public void createEvent() {
        tomlinson = new Location("Tomlinson", 41.504099, -81.609537);
        e1 = new Event("Lunch", tomlinson, 3539, "B100", 12, 10, 5);
        e2 = new Event("Lunch", tomlinson, 3600, "B100", 11, 0, 61);
        e3 = new Event("Lunch", tomlinson, 3600, "B100", 23, 0, 0);
        e4 = new Event(1,1,"Lunch", tomlinson, 3600, "B100", 12, 0, 1);
        e3.setId(2);
        e3.setDayItineraryID(3);
    }

    @Test
    public void testEventMethods(){
        int min = e1.getMin();
        int hour = e1.getHour();
        int sec = e1.getSec();
        int emin = e1.getEndMin();
        int ehour = e1.getEndHour();
        int esec  = e1.getEndSec();
        int length = e1.getEventLength();
        String name = e1.getName();
        String room = e1.getRoomNumber();
        Location loc = e1.getLocation();
        int id4 = e4.getId();
        int did4 = e4.getDayItineraryID();
        int id3 = e3.getId();
        int did3 = e3.getDayItineraryID();
        boolean realTime1 = e1.isRealTime();
        boolean realTime2 = e3.isRealTime();
        boolean realTime3 = e2.isRealTime();
        e2 = new Event("Lunch", tomlinson, 1, "B100", 12, 10, 0);
        boolean isConflict1 = e1.isConflict(e2);
        boolean isConflict2 = e1.isConflict(e4);
        int compare1 = e1.compareTo(e2);
        int compare2 = e1.compareTo(e3);


        assertEquals(10, min);
        assertEquals(5, sec);
        assertEquals(12, hour);
        assertEquals(9, emin);
        assertEquals(4, esec);
        assertEquals(13, ehour);
        assertEquals(length,3539);
        assertEquals("Lunch", name);
        assertEquals("B100", room);
        assertEquals(loc.getName(),tomlinson.getName());
        assertEquals(1,id4);
        assertEquals(1,did4);
        assertEquals(3,did3);
        assertEquals(2,id3);
        assertTrue(realTime1);
        assertFalse(realTime2);
        assertFalse(realTime3);
        assertFalse(isConflict1);
        assertTrue(isConflict2);
        assertEquals(1,compare1);
        assertEquals(-1,compare2);

    }

}