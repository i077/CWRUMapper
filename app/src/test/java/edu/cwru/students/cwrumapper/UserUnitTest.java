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
public class UserUnitTest {

    User user;

    @Test
    public void testStudentCases(){
        user = new User(1,"Amrish");
        //constructor testing for student

        assertEquals("Amrish", user.getName());
        user.addEvent(0,"Jolly", new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
                -81.607163), 100, "100", 9, 0, 0);
        assertEquals(1,user.getId());
        assertTrue(user.isStudent());
        assertEquals(7,user.getItineraries().get(0).getItinerariesForDays().size());
        assertEquals("Jolly", user.getEvents(0).get(0).getName());
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
    }


}