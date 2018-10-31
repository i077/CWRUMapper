package edu.cwru.students.cwrumapper;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final int ERROR_REQUEST_CODE = 9001;

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1111;
    private static final LatLngBounds CWRU_CAMPUS_BOUNDS = new LatLngBounds(
            new LatLng(41.499185, -81.613768), new LatLng(41.517084, -81.602976));
    private static final LatLng CWRU_CAMPUS_CENTER = CWRU_CAMPUS_BOUNDS.getCenter();

    private Context mainContext;
    private boolean mLocationPermissionsGranted = false;

    private GoogleMap mMap;
    private Date mCurrentDate;
    private DayItinerary mCurrentDayItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        // set strict mode to enable API calls
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (checkGooglePlayServices()) {
            getLocationPermission();
        }
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int result = availability.isGooglePlayServicesAvailable(MainActivity.this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (availability.isUserResolvableError(result)) {
            Dialog error = availability.getErrorDialog(MainActivity.this, result,
                    ERROR_REQUEST_CODE);
            error.show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) ==
                PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                for (int i : grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
                initializeMap();
            }
        }
    }

    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // find my location feature: try to achieve this without re-checking permission
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, android.os.Process.myPid(),
                android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMyLocationButtonClickListener(this);

        // show/hide other buttons
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

        // enable map touch gestures
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // set camera bounds and default camera position
        mMap.setLatLngBoundsForCameraTarget(CWRU_CAMPUS_BOUNDS);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CWRU_CAMPUS_CENTER, 15));

        // enable info windows when clicking on markers
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View window = inflater.inflate(R.layout.info_window, null);
                TextView info = (TextView) window.findViewById(R.id.info_window_content);
                info.setText(Html.fromHtml(marker.getSnippet()));
                return window;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        // test
        DayItinerary test = new DayItinerary(0);    // placeholder
        Router.findRoute(test, mainContext);    // hardcoded test case in here
        showRoute(test);
    }

    @Override
    public boolean onMyLocationButtonClick() { return false; }

    @Override
    public void onMyLocationClick(@NonNull Location location) { }

    public void showRoute(DayItinerary dayItin) {
        ArrayList<LatLng> routePoints = dayItin.getRouteInfo().getPoints();
//        ArrayList<Event> events = dayItin.getEvents();

        // test: Taft -> Millis Schmitt -> Alumni -> Kusch
        ArrayList<Event> events = new ArrayList<>();
        Event one = new Event(new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
                -81.607163), 100, 100, 9, 0, 0);
        Event two = new Event(new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
                -81.606873), 100, 0, 12, 0, 0);
        Event three = new Event(new edu.cwru.students.cwrumapper.user.Location("Alumni", 41.500547 ,
                -81.602553), 100, 410, 15, 0, 0);
        Event four = new Event(new edu.cwru.students.cwrumapper.user.Location("Kusch", 41.500787,
                -81.600249), 100, 100, 21, 0, 0);
        events.add(one);
        events.add(two);
        events.add(three);
        events.add(four);

        // draw route
        LatLng start = routePoints.get(0);
        LatLng end;
        for (int i = 1; i < routePoints.size(); i++) {
            end = routePoints.get(i);
            mMap.addPolyline(new PolylineOptions()
                    .add(start, end)
                    .width(5).color(Color.BLUE).geodesic(false));
            start = end;
        }
        setupMarkers(events);

        mCurrentDayItinerary = dayItin;
    }

    private void setupMarkers(@NonNull ArrayList<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            Event current = events.get(i);
            String content = "<big><b>" + current.getLocation().getName() + "</b> " + current.getRoomNumber() + "</big><br>"
                    + getTimeFormat(current.getHour(), current.getMin(), current.getSec()) + " - "
                    + getTimeFormat(current.getEndHour(), current.getEndMin(), current.getEndSec());
            edu.cwru.students.cwrumapper.user.Location loc = current.getLocation();
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .snippet(content));
        }
    }

    @NonNull
    private String getTimeFormat(int h, int m, int s) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(h));
        sb.append(":");
        sb.append(String.format(Locale.getDefault(), "%02d", m));
        if (s < 0) {    // only add seconds to time if they are specified
            sb.append(":");
            sb.append(String.format(Locale.getDefault(), "%02d", s));
        }
        return sb.toString();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
