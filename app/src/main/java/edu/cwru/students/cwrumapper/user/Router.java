package edu.cwru.students.cwrumapper.user;

import java.util.Calendar;

public class Router {



    public static void Route(DayItinerary dayItin){

    }

    public static void Route(DayItinerary dayItin, User user){

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
