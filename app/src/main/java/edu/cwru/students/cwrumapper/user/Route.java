package edu.cwru.students.cwrumapper.user;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Route {

    private ArrayList<LatLng> points;

    public Route(JSONObject json) {
        try {
//            JSONArray routeArray = json.getJSONArray("routes");
//            JSONObject routes = routeArray.getJSONObject(0);
//            JSONObject polylines = routes.getJSONObject("overview_polyline");
//            String encodedString = polylines.getString("points");
            String encodedString = json.getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points");
            points = decodePoints(encodedString);
        } catch (JSONException e) {

        }
    }

    private ArrayList<LatLng> decodePoints(String encodedPoints) {
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

    public ArrayList<LatLng> getPoints() {
        return points;
    }
}
