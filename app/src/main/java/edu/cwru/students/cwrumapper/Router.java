package edu.cwru.students.cwrumapper;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Location;
import edu.cwru.students.cwrumapper.user.Route;

public class Router {

    public static void findRoute(DayItinerary dayItin, Context context) {

        StringBuilder urlBuilder = new StringBuilder();
//        ArrayList<Event> routeEvents = dayItin.getEvents();

        // test: Taft -> Millis Schmitt -> Alumni -> Kusch
        ArrayList<Event> routeEvents = new ArrayList<>();
        Event one = new Event(new Location("Taft", 41.512771,
                -81.607163), 100, 100, 9, 0, 0);
        Event two = new Event(new Location("Millis Schmitt", 41.504099,
                -81.606873), 100, 0, 12, 0, 0);
        Event three = new Event(new Location("Alumni", 41.500547 ,
                -81.602553), 100, 410, 15, 0, 0);
        Event four = new Event(new Location("Kusch", 41.500787,
                -81.600249), 100, 100, 21, 0, 0);
        routeEvents.add(one);
        routeEvents.add(two);
        routeEvents.add(three);
        routeEvents.add(four);

        // base for Directions API call
        urlBuilder.append("https://maps.googleapis.com/maps/api/directions/json?");
        // start point
        Location origin = routeEvents.get(0).getLocation();
        urlBuilder.append("origin=" + origin.getLatitude() + "," + origin.getLongitude() + "&");
        // end point
        Location destination = routeEvents.get(routeEvents.size() - 1).getLocation();
        urlBuilder.append("destination=" + destination.getLatitude() + ","
                + destination.getLongitude() + "&");

        // set mode (always walking)
        urlBuilder.append("mode=walking&");

        // intermediate points throughout day
        if (routeEvents.size() > 2) {
            urlBuilder.append("waypoints=");
            for (int i = 1; i < routeEvents.size() - 1; i++) {
                Location waypoint = routeEvents.get(i).getLocation();
                urlBuilder.append(waypoint.getLatitude() + "," + waypoint.getLongitude());
                if (i < routeEvents.size() - 2) {
                    urlBuilder.append("|");
                }
            }
            urlBuilder.append("&");
        }

        // key (RESTRICT KEY AFTER FINISHED TESTING!)
//        urlBuilder.append("key=" + context.getResources().getString(R.string.google_maps_api_key));
        urlBuilder.append("key=AIzaSyDNbP-3OzqyB2PfR4_XElCfdhAtRCfNC2o");

        // read JSON file from URL
        try (InputStream stream = new URL(urlBuilder.toString()).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder jsonBuilder = new StringBuilder();
            int character;

            while ((character = reader.read()) != -1) {
                jsonBuilder.append((char) character);
            }

            JSONObject json = new JSONObject(jsonBuilder.toString());
            dayItin.updateRouteInfo(new Route(json));
//            ((MainActivity) context).showRoute(dayItin);
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        } catch (JSONException e) {

        }
    }
}
