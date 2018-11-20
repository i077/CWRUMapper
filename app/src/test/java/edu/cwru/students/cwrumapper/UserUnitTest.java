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
        user = new User(15,"Amrish");
        //constructor testing for student

        assertEquals("Amrish", user.getName());
        assertEquals(15,user.getId());
        assertTrue(user.isStudent());
        assertEquals(7,user.getItineraries().get(0).getItinerariesForDays().size());
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