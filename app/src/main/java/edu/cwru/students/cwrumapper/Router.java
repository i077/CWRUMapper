package edu.cwru.students.cwrumapper;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.cwru.students.cwrumapper.user.Building;
import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Location;

public class Router {

    private final static String URL_BASE = "https://maps.googleapis.com/maps/api/directions/json?";

    // TODO Consider changing method parameters to take ArrayList<Event> instead of DayItinerary
    /**
     * Calculates the route that corresponds to the Events in the given DayItinerary.
     *
     * A note about terminology:
     * Routes can be broken down into segments or partitions. A segment is a portion of a route
     * that goes between two consecutive events. Therefore, an n-event route will always be
     * comprised of (n-1) segments. A partition consists of one or more segments. A route is
     * partitioned if it is optimal to enter and exit a building associated with an event at
     * different locations. So partition boundaries occur only at these types of buildings.
     *
     * @param dayItin - DayItinerary whose events are being routed
     * @param apiKey - API key needed to complete Directions API call
     * @return list of points needed to draw entire route polyline on a Google Map, given in
     * partitions
     */
    public static ArrayList<ArrayList<LatLng>> findRoute(DayItinerary dayItin, String apiKey) {
//        ArrayList<Event> routeEvents = dayItin.getEvents();
//
//        // DayItinerary must consist of at least 2 events
//        if (routeEvents.size() < 2) {
//            return null;
//        }

        // TODO Remove hardcoded test cases, uncomment lines above after implementing Building in Event
        // This route does not correspond to hardcoded test case in MainActivity.
//        Event one = new Event("Dorm", new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
//                -81.607163), 100, "100", 9, 0, 0);
//        Event two = new Event("Club Meeting", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
//                -81.606873), 100, "0", 12, 0, 0);
//        Event three = new Event("EECS 132", new edu.cwru.students.cwrumapper.user.Location("Alumni", 41.500547 ,
//                -81.602553), 100, "410", 23, 0, 0);

        // TODO Remove hardcoded test events
        edu.cwru.students.cwrumapper.user.Location taft = new Location("Taft", new LatLng[]{
                new LatLng(41.512756, -81.607186)});
        edu.cwru.students.cwrumapper.user.Location mshmitt = new Location("Millis Schmitt", new LatLng[]{
                new LatLng(41.504099, -81.606873),
                new LatLng(41.503729, -81.607005)});
        edu.cwru.students.cwrumapper.user.Location kusch = new Location("Kusch", new LatLng[]{
                new LatLng(41.500787, -81.600249)});
        edu.cwru.students.cwrumapper.user.Location alumni = new Location("Alumni", new LatLng[]{                    // SRV
                new LatLng(41.500547, -81.602553)});
        Event one = new Event("Dorm", taft, 100, "100", 9, 0, 0);
        Event two = new Event("EECS 132", mshmitt, 100, "0", 12, 0, 0);
        Event three = new Event("Club Meeting", alumni, 100, "410", 15, 0, 0);
        Event four = new Event("DANK 420", kusch, 100, "100", 21, 0, 0);
        Event five = new Event("EECS 132 (again)", mshmitt, 100, "0", 23, 0, 0);

        ArrayList<Event> routeEvents = new ArrayList<>();
        routeEvents.add(one);
        routeEvents.add(two);
        routeEvents.add(three);
        routeEvents.add(four);
        routeEvents.add(five);

        ArrayList<LatLng[]> routeSegments = findRouteSegments(routeEvents);
        ArrayList<ArrayList<LatLng>> routePartitions = partitionRoute(routeSegments);
        ArrayList<ArrayList<LatLng>> routePolyline = new ArrayList<>();

        for (ArrayList<LatLng> part : routePartitions) {
            ArrayList<ArrayList<LatLng>> partPolyline = getPartitionPolyline(part, apiKey);
            if (partPolyline == null) {
                return null;
            } else {
                routePolyline.addAll(partPolyline);    // add polyline partition
            }
        }
        return routePolyline;
    }

    /**
     * Each pair represents a movement between two different events (therefore the second entry
     * of a certain pair corresponds to the same event/building as the first entry of the
     * next pair).
     *
     * @param events - user Events specified in the DayItinerary
     * @return a list of pairs of points for the entire route
     */
    private static ArrayList<LatLng[]> findRouteSegments(ArrayList<Event> events) {

        // TODO Remove hardcoded test cases.
        // test buildings correspond to events in findRoute (Event class cannot support Building)
        LatLng[] taftArray = {new LatLng(41.512771,-81.607163)};
        Building taft = new Building("Taft", taftArray);
        LatLng[] tinkArray = {new LatLng(41.508757, -81.608493), new LatLng(41.507596, -81.608756)};
        Building tink = new Building("Tinkham Veale", tinkArray);
        LatLng[] millisArray = {new LatLng(41.504099,-81.606873)};
        Building millis = new Building("Millis Schmitt", millisArray);
        LatLng[] alumniArray = {new LatLng(41.500547,-81.602553)};
        Building alumni = new Building("Alumni", alumniArray);
        LatLng[] dank420Array = {new LatLng(41.500787,-81.600249)};
        Building kusch = new Building("Kusch", dank420Array);

//        Building[] buildings = {taft, tink, millis};
        Building[] buildings = {taft, millis, alumni, kusch, millis};

        ArrayList<LatLng[]> pairs = new ArrayList<>();
        Event prev = events.get(0);
        for (int i = 1; i < events.size(); i++) {
            Event current = events.get(i);
//            pairs.add(closestPair(prev.getBuilding(), current.getBuilding()));
            // TODO Remove line below and uncomment line above after implementing Building in Event
            pairs.add(closestPair(buildings[i - 1], buildings[i]));
            prev = current;
        }
        for (LatLng[] l : pairs) {
            System.out.println("Pair: " + l[0] + ", " + l[1]);
        }
        return pairs;
    }

    /**
     * Calculates all possible straight-line distances between entrances of two buildings
     * and returns the pair of entrances with the shortest distance.
     * This method assumes that in the set of all possible entrance pairs between the two
     * given buildings, the pair of entrances with the shortest straight-line distance is also
     * the shortest-distance pair as routed by the Directions API. Although this heuristic is
     * not always true, it should suffice for the majority of cases.
     *
     * @param first - the origin building
     * @param second - the destination building
     * @return the pair of entrances between the two buildings with the shortest straight-line
     * distance between them
     */
    private static LatLng[] closestPair(Building first, Building second) {

        LatLng firstEnt = null;
        LatLng secondEnt = null;
        double distance;
        double minimum = Double.POSITIVE_INFINITY;
        for (LatLng a : first.getEntrances()) {
            for (LatLng b : second.getEntrances()) {
                distance = Math.sqrt(Math.pow(a.latitude - b.latitude, 2) + Math.pow(a.longitude - b.longitude, 2));
                if (distance < minimum) {
                    firstEnt = a;
                    secondEnt = b;
                    minimum = distance;
                }
            }
        }
        LatLng[] array = {firstEnt, secondEnt};
        return array;
    }

    /**
     * An API call will be partitioned in the case that the optimal route states the user
     * should enter and exit an event/building at different locations. Splitting the call
     * at this point prevents unnecessary routing between entrances/exits of a single building.
     *
     * @param segments - list of LatLng pairs, each representing movement between two Events
     * @return a list of sublists, each containing the points to be grouped into one Directions
     * API call
     */
    private static ArrayList<ArrayList<LatLng>> partitionRoute(ArrayList<LatLng[]> segments) {
        ArrayList<ArrayList<LatLng>> partitions = new ArrayList<>();
        ArrayList<LatLng> part = new ArrayList<>();

        LatLng[] prev = segments.get(0);
        part.add(prev[0]);
        for (int i = 1; i < segments.size(); i++) {
            LatLng[] current = segments.get(i);
            LatLng in = prev[1];
            LatLng out = current[0];
            if (!(in.latitude == out.latitude &&
                    in.longitude == out.longitude)) {
                part.add(in);
                partitions.add(part);
                part = new ArrayList<>();    // new route partition
            }
            part.add(out);
            prev = current;
        }
        part.add(segments.get(segments.size() - 1)[1]);    // add final point
        partitions.add(part);

        System.out.println("Partitions: " + partitions.size());
        for (ArrayList<LatLng> l : partitions) {
            System.out.println(l.get(1) + " -> " + l.get(l.size() - 1));
        }
        return partitions;
    }

    /**
     * Calls the Directions API to find the route and corresponding polyline for the specified
     * route partition.
     *
     * @param routePoints - coordinates of points that must be routed in this partition
     * @param key - Google API key needed to complete Directions API request
     * @return list of coordinates needed to draw this partition of the entire route, each
     * element of the returned list is a segment of the partition
     */
    private static ArrayList<ArrayList<LatLng>> getPartitionPolyline(ArrayList<LatLng> routePoints, String key) {

        StringBuilder urlBuilder = new StringBuilder();

        // base
        urlBuilder.append(URL_BASE);

        // start point of partition
        LatLng origin = routePoints.get(0);
        urlBuilder.append("origin=")
                .append(origin.latitude)
                .append(",")
                .append(origin.longitude)
                .append("&");

        // end point of partition
        LatLng destination = routePoints.get(routePoints.size() - 1);
        urlBuilder.append("destination=")
                .append(destination.latitude)
                .append(",")
                .append(destination.longitude)
                .append("&");

        // set mode (always walking)
        urlBuilder.append("mode=walking&");

        // intermediate points within partition
        if (routePoints.size() > 2) {
            urlBuilder.append("waypoints=");
            for (int i = 1; i < routePoints.size() - 1; i++) {
                LatLng waypoint = routePoints.get(i);
                urlBuilder.append(waypoint.latitude)
                        .append(",")
                        .append(waypoint.longitude);
                if (i < routePoints.size() - 2) {
                    urlBuilder.append("|");
                }
            }
            urlBuilder.append("&");
        }

        // key (RESTRICT KEY AFTER FINISHED TESTING!)
        urlBuilder.append("key=")
                .append(key);

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
            JSONArray routes = json.getJSONArray("routes");
            if (routes.length() <= 0) {
                return null;
            }
            JSONArray legs = routes.getJSONObject(0)
                    .getJSONArray("legs");

            ArrayList<ArrayList<LatLng>> segments = new ArrayList<>();    // segments in partition
            ArrayList<LatLng> seg = new ArrayList<>();    // individual segment

            for (int i = 0; i < legs.length(); i++) {
                JSONArray steps = legs.getJSONObject(i)
                        .getJSONArray("steps");
                for (int j = 0; j < steps.length(); j++) {
                    String encodedString = steps.getJSONObject(j)
                            .getJSONObject("polyline")
                            .getString("points");
                    seg.addAll(decodePoints(encodedString));
                }
                segments.add(seg);
                seg = new ArrayList<>();
            }
            return segments;    // return segments in this partition
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        } catch (JSONException e) {

        }
        return null;
    }

    /**
     * The Google Maps API encodes polylines as strings as specified on their website:
     * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
     *
     * @param encodedPoints - the encoded string for the polyline from the raw JSON file
     * @return list of coordinates of points represented by encodedPoints
     */
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
