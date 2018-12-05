package edu.cwru.students.cwrumapper.user;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Building {

    private String name;

    // latitudes and longitudes of all entrances to this building
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    public Building(String n, LatLng[] lls) {
        name = n;

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        for (int i = 0; i < lls.length; i++) {
            LatLng e = lls[i];
            latitudes.add(e.latitude);
            longitudes.add(e.longitude);
        }
    }

    public String getName() { return name; }

    public ArrayList<LatLng> getEntrances() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < latitudes.size(); i++) {
            latLngs.add(new LatLng(latitudes.get(i), longitudes.get(i)));
        }
        return latLngs;
    }
}
