package edu.cwru.students.cwrumapper;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

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

    public static void findRoute(DayItinerary dayItin, String apiKey) {

        StringBuilder urlBuilder = new StringBuilder();
//        ArrayList<Event> routeEvents = dayItin.getEvents();

        // test: Taft -> Millis Schmitt -> Alumni -> Kusch
        ArrayList<Event> routeEvents = new ArrayList<>();
        Event one = new Event("Dorm", new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
                -81.607163), 100, "100", 9, 0, 0);
        Event two = new Event("EECS 132", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
                -81.606873), 100, "0", 12, 0, 0);
        Event three = new Event("Club Meeting", new edu.cwru.students.cwrumapper.user.Location("Alumni", 41.500547 ,
                -81.602553), 100, "410", 15, 0, 0);
        Event four = new Event("DANK 420", new edu.cwru.students.cwrumapper.user.Location("Kusch", 41.500787,
                -81.600249), 100, "100", 21, 0, 0);
        Event five = new Event("EECS 132 (again)", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
                -81.606873), 100, "0", 22, 0, 0);
        routeEvents.add(one);
        routeEvents.add(two);
        routeEvents.add(three);
        routeEvents.add(four);
        routeEvents.add(five);

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
        urlBuilder.append("key=" + apiKey);

        // read JSON file from URL
        try (InputStream stream = new URL(urlBuilder.toString()).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder jsonBuilder = new StringBuilder();
            int character;

            while ((character = reader.read()) != -1) {
                jsonBuilder.append((char) character);
            }

            // parse JSON for route
            JSONObject json = new JSONObject(jsonBuilder.toString());
            String encodedString = json.getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points");
            ArrayList<LatLng> points = decodePoints(encodedString);

            // convert LatLng to Doubles to be stored in database
            ArrayList<Double> lats = new ArrayList<>();
            ArrayList<Double> longs = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                LatLng l = points.get(i);
                lats.add(l.latitude);
                longs.add(l.longitude);
            }

            // update route
//            dayItin.updateRouteInfo(lats, longs);
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        } catch (JSONException e) {

        }
    }

    private static ArrayList<LatLng> decodePoints(String encodedPoints) {
        ArrayList<LatLng> decodedPoints = new ArrayList<>();
        int index = 0;
        int len = encodedPoints.length();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = encodedPoints.charAt(index) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                index++;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPoints.charAt(index) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                index++;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));

            decodedPoints.add(p);
        }
        return decodedPoints;
    }
}
