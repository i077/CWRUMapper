package edu.cwru.students.cwrumapper.user;

import com.google.android.gms.maps.model.LatLng;

public class Building {

    private String name;

    // latitudes and longitudes of all entrances to this building
    private double[] latitudes;
    private double[] longitudes;

    public Building(String n, LatLng[] lls) {
        name = n;

        latitudes = new double[lls.length];
        longitudes = new double[lls.length];
        for (int i = 0; i < lls.length; i++) {
            LatLng e = lls[i];
            latitudes[i] = e.latitude;
            longitudes[i] = e.longitude;
        }
    }

    public String getName() { return name; }

    public LatLng[] getEntrances() {
        LatLng[] latLngs = new LatLng[latitudes.length];
        for (int i = 0; i < latLngs.length; i++) {
            latLngs[i] = new LatLng(latitudes[i], longitudes[i]);
        }
        return latLngs;
    }
}
