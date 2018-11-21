package edu.cwru.students.cwrumapper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;

import edu.cwru.students.cwrumapper.user.Itinerary;
import edu.cwru.students.cwrumapper.user.Repository;
import edu.cwru.students.cwrumapper.user.User;
import edu.cwru.students.cwrumapper.user.UserDatabase;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MainActivity";

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

    private Repository dataRepo;

    private RecyclerView mItineraryRecyclerView;
    private RecyclerView.Adapter mItineraryAdapter;
    private RecyclerView.LayoutManager mItineraryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        // Get current time
        Calendar mCalendar = Calendar.getInstance();
        mCurrentDate = mCalendar.getTime();

        // Initialize repository structure from persistent storage
        dataRepo = new Repository(getApplication());
        User user = dataRepo.fetchUser(0).getValue();
        if (user == null) {
            // TODO Throw to SignInActivity since there is no user
            user = new User(0, "Tester");
            user.student = true;
        }

        // set strict mode to enable API calls
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (checkGooglePlayServices()) {
            getLocationPermission();
        }

        // Inflate bottom sheet
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(600);

        // TODO Remove hardcoded test events
        Event one = new Event("Dorm", new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
                -81.607163), 100, "100", 9, 0, 0);
        Event two = new Event("EECS 132", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
                -81.606873), 100, "0", 12, 0, 0);
        Event three = new Event("Club Meeting", new edu.cwru.students.cwrumapper.user.Location("Alumni", 41.500547 ,
                -81.602553), 100, "410", 15, 0, 0);
        Event four = new Event("DANK 420", new edu.cwru.students.cwrumapper.user.Location("Kusch", 41.500787,
                -81.600249), 100, "100", 21, 0, 0);

        mCurrentDayItinerary = user.getItineraries().get(0)
                .getItinerariesForDays()
                .get(mCalendar.get(Calendar.DAY_OF_WEEK));
        mCurrentDayItinerary.addEvent(one);
        mCurrentDayItinerary.addEvent(two);
        mCurrentDayItinerary.addEvent(three);
        mCurrentDayItinerary.addEvent(four);

        // Inflate contents of bottom sheet
        TextView mDateTextView = findViewById(R.id.date_text);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d");
        mDateTextView.setText(dateFormat.format(mCurrentDate));

        // Inflate RecyclerView for Itinerary
        mItineraryRecyclerView = findViewById(R.id.recyclerview_itinerary);
        // Each event will take up the same space
        mItineraryRecyclerView.setHasFixedSize(true);
        // Use a linear layout manager to inflate recycler view
        mItineraryLayoutManager = new LinearLayoutManager(this);
        mItineraryRecyclerView.setLayoutManager(mItineraryLayoutManager);
        refreshItinerary();

        // Set edit button's onClick listener
        Button editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditItineraryActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

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
                TextView info = window.findViewById(R.id.info_window_content);
                info.setText(Html.fromHtml(marker.getSnippet()));
                return window;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        // test
        DayItinerary test = new DayItinerary();    // placeholder
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
        Event one = new Event("Dorm", new edu.cwru.students.cwrumapper.user.Location("Taft", 41.512771,
                -81.607163), 100, "100", 9, 0, 0);
        Event two = new Event("EECS 132", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", 41.504099,
                -81.606873), 100, "0", 12, 0, 0);
        Event three = new Event("Club Meeting", new edu.cwru.students.cwrumapper.user.Location("Alumni", 41.500547 ,
                -81.602553), 100, "410", 15, 0, 0);
        Event four = new Event("DANK 420", new edu.cwru.students.cwrumapper.user.Location("Kusch", 41.500787,
                -81.600249), 100, "100", 21, 0, 0);
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

    /**
     * Refresh Itinerary RecyclerView with list of remaining events for today.
     * Called when activity starts or resumes (from editing the itinerary).
     */
    private void refreshItinerary() {
        ArrayList<Event> eventsFromNow = new ArrayList<>();

        // Get remaining events for day
        for (Event e : mCurrentDayItinerary.getEvents()) {
            // Get time for event's current day's instance
            Calendar eventInstance = Calendar.getInstance();
            eventInstance.set(Calendar.HOUR, e.getHour());
            eventInstance.set(Calendar.MINUTE, e.getMin());
            Date eventInstanceTime = eventInstance.getTime();

            // Compare to current time, and add to event set (for RecyclerView) if after
            if (eventInstanceTime.after(mCurrentDate)) {
                eventsFromNow.add(e);
                Log.v(TAG, "Added event " + e.getName());
            }
        }

        // Update adapter to plug new event set to RecyclerView,
        // passing in all events for today from now
        mItineraryAdapter = new ItineraryMainAdapter(eventsFromNow);
        mItineraryRecyclerView.setAdapter(mItineraryAdapter);
        // Hide placeholder if necessary
        if (eventsFromNow.size() > 0) {
            TextView mPlaceholderText = findViewById(R.id.text_placeholder_itinerary);
            ((ViewGroup) mPlaceholderText.getParent()).removeView(mPlaceholderText);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
