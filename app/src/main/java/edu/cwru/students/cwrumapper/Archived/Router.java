package edu.cwru.students.cwrumapper.Archived;

import java.util.Calendar;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.User;

public class Router {



    public static void route(DayItinerary dayItin){

    }

    public static void route(DayItinerary dayItin, User user){

    }

    public static int pastRoute(DayItinerary dayItin){
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        return 1;
    }

    public static int futureRoute(DayItinerary dayItin){
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        return 1;
    }

    public static int currentRoute(DayItinerary dayItin){
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        return 1;
    }
}
