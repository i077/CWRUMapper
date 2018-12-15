package edu.cwru.students.cwrumapper;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
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
        loc = new Location("Millis Schmitt", new LatLng[]{
                new LatLng(41.504099, -81.606873),
                new LatLng(-81.607005, -81.607005)});
        ArrayList<Double> lat = loc.getLatitudes();
        ArrayList<Double> lon = loc.getLongitudes();
        ArrayList<Double> lat1 = DataTypeConverter.fromString(DataTypeConverter.fromArrayList(lat));
        String name = loc.getName();
        Location[] locations = Location.populateData();

        assertEquals(lat,lat1);
        assertEquals(-81.607005,lat.get(1),.00001);
        assertEquals(-81.607005,lon.get(1),.00001);
        assertEquals("Millis Schmitt",name);

        loc = new Location("Taft");
        assertEquals("Taft",loc.getName());
        loc = new Location("Nord Hall",lat,lon);
        assertEquals("Nord Hall", loc.getName());
        loc = Location.getLocationByName("Taft");
        String[] names = Location.getLocationNames();
        assertEquals("Taft",loc.getName());


    }



}