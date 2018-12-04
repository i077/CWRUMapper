package edu.cwru.students.cwrumapper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Itinerary;
import edu.cwru.students.cwrumapper.user.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ItineraryUnitTest {

    Itinerary temp;

    @Before
    public void setup(){

    }



    @Test
    public void testItineraryConstructor(){
        //test for simple adding of event
        temp = new Itinerary();
        Calendar cal = Calendar.getInstance();
        temp = new Itinerary(cal,3);
        temp = new Itinerary(cal, 10);
        ArrayList<DayItinerary> dayItineraries = temp.getItinerariesForDays();
        temp = new Itinerary(1,2,dayItineraries,false,cal,0);
        int id = temp.getId();
        int userId = temp.getUserID();
        assertEquals(1,id);
        assertEquals(2,userId);

    }

    @Test
    public void testItineraryManipulationStudent(){
        temp = new Itinerary();
        temp.setId(1);
        temp.setUserID(2);

        boolean add = temp.addDay();
        boolean delete = temp.deleteDay();
        int id = temp.getId();
        int userId = temp.getUserID();
        int length = temp.getLengthOfStay();

        assertFalse(add);
        assertFalse(delete);
        assertEquals(1,id);
        assertEquals(2,userId);
        assertEquals(0,length);

    }

    @Test
    public void testItineraryManipulationGuest(){
        Calendar cal = Calendar.getInstance();
        temp = new Itinerary(cal,3);
        cal.set(Calendar.YEAR,2019);
        temp.editStartDay(cal);
        Calendar cal2 = temp.getStartDate();
        temp.setId(1);
        temp.setUserID(0);

        boolean add = temp.addDay();
        int length = temp.getLengthOfStay();
        assertEquals(4,length);
        boolean delete = temp.deleteDay();
        int id = temp.getId();
        int userId = temp.getUserID();
        length = temp.getLengthOfStay();

        assertTrue(add);
        assertTrue(delete);
        assertEquals(1,id);
        assertEquals(0,userId);
        assertEquals(3,length);

    }

}
