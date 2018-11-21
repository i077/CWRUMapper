package edu.cwru.students.cwrumapper;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.*;

import static org.junit.Assert.*;

/**
 * Test functions of User and Itinerary Classes
 */
public class LocationUnitTest {

    private Location loc;

    @Test
    public void testLocation(){
        loc = new Location("Tomlinson",41.504099,-81.609537 );
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();
        String name = loc.getName();

        assertEquals(41.504099,lat,.00001);
        assertEquals(-81.609537,lon,.00001);
        assertEquals("Tomlinson",name);
    }



}