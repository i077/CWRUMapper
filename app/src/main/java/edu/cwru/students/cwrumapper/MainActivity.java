package edu.cwru.students.cwrumapper;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;

import edu.cwru.students.cwrumapper.user.Repository;
import edu.cwru.students.cwrumapper.user.User;

import static java.time.temporal.ChronoUnit.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MainActivity";

    private static final int ERROR_REQUEST_CODE = 9001;

    private static final int SIGNIN_REQUEST_CODE = 2;

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1111;
    private static final LatLngBounds CWRU_CAMPUS_BOUNDS = new LatLngBounds(
            new LatLng(41.499054, -81.615440), new LatLng(41.515922, -81.598960));
    private static final LatLng CWRU_CAMPUS_CENTER = CWRU_CAMPUS_BOUNDS.getCenter();

    private static final int PEEK_HEIGHT = 600;

    private GoogleMap mMap;
    private LocalDateTime mCurrentTime;
    private DayItinerary mCurrentDayItinerary;
    private ArrayList<Event> eventsFromNow;
    private User mUser;

    private Repository dataRepo;

    private RecyclerView mItineraryRecyclerView;
    private RecyclerView.Adapter mItineraryAdapter;
    private RecyclerView.LayoutManager mItineraryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get current time
        mCurrentTime = LocalDateTime.now();

        // Initialize repository structure from persistent storage
        dataRepo = new Repository(getApplication());
        if (dataRepo.getUser(0) == null) { // No user -- user database is empty.
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivityForResult(signInIntent, SIGNIN_REQUEST_CODE);

//            // TODO Remove hardcoded test events
//
//            edu.cwru.students.cwrumapper.user.Location taft = dataRepo.getLocation("Taft");
//            edu.cwru.students.cwrumapper.user.Location mshmitt = dataRepo.getLocation("Millis Schmitt");
//            edu.cwru.students.cwrumapper.user.Location kusch = dataRepo.getLocation("Kusch");
//            edu.cwru.students.cwrumapper.user.Location alumni = dataRepo.getLocation("Alumni");
//            Event one = new Event("Dorm", taft, 100, "100", 9, 0, 0);
//            Event two = new Event("EECS 132", mshmitt, 100, "0", 12, 0, 0);
//            Event three = new Event("Club Meeting", alumni, 100, "410", 15, 0, 0);
//            Event four = new Event("DANK 420", kusch, 100, "100", 21, 0, 0);
//            Event five = new Event("EECS 132 (again)", mshmitt, 50, "0", 23, 0, 0);
//
//            mCurrentDayItinerary = user.getItineraries().get(0)
//                    .getItinerariesForDays()
//                    .get(mCurrentTime.getDayOfWeek().getValue() - 1);
//            mCurrentDayItinerary.addEvent(one);
//            mCurrentDayItinerary.addEvent(two);
//            mCurrentDayItinerary.addEvent(three);
//            mCurrentDayItinerary.addEvent(four);
//            mCurrentDayItinerary.addEvent(five);
//
//            dataRepo.insertUser(user);
        } else { // We have a user, so load it
            mUser = dataRepo.getUser(0);
            setupActivity();
        }
    }

    private void setupActivity() {
        mCurrentDayItinerary = mUser.getItineraries().get(0)
                .getItinerariesForDays()
                .get(mCurrentTime.getDayOfWeek().getValue() - 1);

        // set strict mode to enable API calls
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (checkGooglePlayServices()) {
            getLocationPermission();
        }

        // Inflate bottom sheet
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(PEEK_HEIGHT);


        // Inflate RecyclerView for Itinerary
        mItineraryRecyclerView = findViewById(R.id.recyclerview_itinerary);
        // Each mEvent will take up the same space
        mItineraryRecyclerView.setHasFixedSize(true);
        // Use a linear layout manager to inflate recycler view
        mItineraryLayoutManager = new LinearLayoutManager(this);
        mItineraryRecyclerView.setLayoutManager(mItineraryLayoutManager);
        refreshSheet();

        // Set edit button's onClick listener
        Button editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditItineraryActivity.class);
            intent.putExtra("userID", mUser.getId());
            intent.putExtra("dayOfWeek", mCurrentTime.getDayOfWeek().getValue());
            MainActivity.this.startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentTime = LocalDateTime.now();
        mUser = dataRepo.getUser(0);
        if (mUser != null) {
            mCurrentDayItinerary = mUser.getItineraries().get(0)
                    .getItinerariesForDays()
                    .get(mCurrentTime.getDayOfWeek().getValue() - 1);
            refreshSheet();
            initializeMap();
        }
//        showRoute(mCurrentDayItinerary);
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
                        return;
                    }
                }
                initializeMap();
            }
        }
    }

    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        // re-size map fragment so that elements are centered in visible portion of the map view
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = getApplicationContext().getResources().getDisplayMetrics().heightPixels - PEEK_HEIGHT + 100;
        mapFragment.getView().setLayoutParams(params);

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

        // set camera and view settings
        mMap.setLatLngBoundsForCameraTarget(CWRU_CAMPUS_BOUNDS);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CWRU_CAMPUS_CENTER, 15));
        mMap.setMinZoomPreference(14.0f);

        // enable info windows when clicking on markers
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View window = inflater.inflate(R.layout.info_window, null);
                TextView info = window.findViewById(R.id.info_window_content);
                info.setText(Html.fromHtml(marker.getSnippet(), Html.FROM_HTML_MODE_LEGACY));
                return window;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        // Calculate and show route from current day's itinerary
        showRoute(mCurrentDayItinerary);
    }

    @Override
    public boolean onMyLocationButtonClick() { return false; }

    @Override
    public void onMyLocationClick(@NonNull Location location) { }

    /**
     * Calculate and display route based on given list of events.
     * @param dayItin - DayItinerary to be displayed on the map
     * @return true if route was updated and displayed successfully, otherwise false
     */
    public boolean showRoute(@NonNull DayItinerary dayItin) {

        // calculate route
        ArrayList<ArrayList<LatLng>> routeSegments = Router.findRoute(dayItin,
                getResources().getString(R.string.google_maps_api_key));
        if (routeSegments == null) {
            return false;
        }

        ArrayList<LatLng> routePoints = new ArrayList<>();
        routePoints.add(routeSegments.get(0).get(0));    // add location of first event

        int nextEvent = dayItin.getEvents().size() - eventsFromNow.size() - 1;    // index of next event at the time method is called
        int segColor;
        int segIndex;
//        ArrayList<LatLng> nextSeg = routeSegments.get(nextEvent);

        for (int i = 0; i < routeSegments.size(); i++) {
            ArrayList<LatLng> seg = routeSegments.get(i);

            // segments associated with elapsed events are grayed out
            if (i < nextEvent) {
                segColor = Color.GRAY;
                segIndex = 0;
            } else if (i == nextEvent) {
                segColor = Color.parseColor("#0D47A1");
                segIndex = 2;
            } else {
                segColor = Color.parseColor("#2196F3");
                segIndex = 1;
            }
            routePoints.add(drawSegment(seg, segColor, segIndex));
        }

        // draw black polylines between entrances of the same building
        ArrayList<LatLng> start = routeSegments.get(0);
        ArrayList<LatLng> end;
        for (int i = 1; i < routeSegments.size(); i++) {
            end = routeSegments.get(i);
            mMap.addPolyline(new PolylineOptions()
                    .add(start.get(start.size() - 1), end.get(0))
                    .width(5).color(Color.BLACK).geodesic(false));
            start = end;
        }

        // next route segment must b re-drawn "on top" of all other segments
//        drawSegment(nextSeg, Color.GREEN);

        setupMarkers(dayItin.getEvents(), routePoints);

//        mCurrentDayItinerary = dayItin;
        return true;
    }

    /**
     * Draws the polyline on the Google map using the given points. Also draws a gray line from
     * the end of the last partition to the beginning of this one (not a super necessary
     * feature).
     *
     * @param points - points to draw this partition polyline
     */
    private LatLng drawSegment(ArrayList<LatLng> points, int color, int index) {
        LatLng start = points.get(0);
        LatLng end = points.get(1);

        for (int i = 1; i < points.size(); i++) {
            end = points.get(i);
            mMap.addPolyline(new PolylineOptions()
                    .add(start, end)
                    .width(10).color(color).zIndex(index).geodesic(false));
            start = end;
        }
        return end;
    }

    /**
     * Place Google Map markers on locations of future events.
     * @param events - list of future events
     */
    private void setupMarkers(@NonNull ArrayList<Event> events, ArrayList<LatLng> points) {
        HashMap<LatLng, MarkerOptions> markerMap = new HashMap<>();

        int nextEvent = events.size() - eventsFromNow.size();    // index of next event at the time method is called
        float opacity;

        System.out.println("Upcoming events: " + eventsFromNow.size());

        for (int i = 0; i < points.size(); i++) {
            Event currEvent = events.get(i);

            // markers corresponding to past events are faded
            if (i < nextEvent) {
                opacity = 0.3f;
            } else {
                opacity = 1;
            }

            String content = "<big><b>" + currEvent.getName() + "</b></big><br>"
                    + currEvent.getLocation().getName() + " " + currEvent.getRoomNumber() + "<br>"
                    + getTimeFormat(currEvent.getHour(), currEvent.getMin(), currEvent.getSec()) + " - "
                    + getTimeFormat(currEvent.getEndHour(), currEvent.getEndMin(), currEvent.getEndSec());

            // combine info windows of duplicate locations
            LatLng currPoint = points.get(i);
            if (markerMap.containsKey(currPoint)) {
                MarkerOptions prev = markerMap.get(currPoint);
                content = prev.getSnippet() + "<p>" + content;
            }

            MarkerOptions newMarker = new MarkerOptions()
                    .position(currPoint)
                    .snippet(content)
                    .alpha(opacity);
            markerMap.put(currPoint, newMarker);
        }

        // add all markers
        for (LatLng l : markerMap.keySet()) {
            mMap.addMarker(markerMap.get(l));
        }
    }

    /**
     * Write hours, minutes, and seconds in a digital-clock format.
     * @param h - hour
     * @param m - minutes
     * @param s - seconds
     * @return String of formatted time
     */
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
     * Refresh bottom sheet. Called when activity starts or resumes.
     */
    private void refreshSheet() {
        // Inflate contents of bottom sheet
        TextView mDateTextView = findViewById(R.id.date_text);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault());
        mDateTextView.setText(dateFormat.format(mCurrentTime));

        // Refresh itinerary with remaining events, updating eventsFromNow
        refreshItinerary();

        // Get time to next mEvent and update textview
        TextView mNextEventView = findViewById(R.id.next_event);
        TextView mNextEventLocView = findViewById(R.id.next_event_location);
        if (eventsFromNow.isEmpty()) {
            mNextEventView.setText(R.string.next_event_none_remain);
            mNextEventLocView.setText("");
        } else {
            Event nextEvent = eventsFromNow.get(0);
            // Construct LocalTime instance to compare time to
            LocalTime nextEventTime = LocalTime.of(nextEvent.getHour(), nextEvent.getMin());
            LocalDateTime nextEventDate = nextEventTime.atDate(mCurrentTime.toLocalDate());

            // Set text values
            long minsUntilEvent = mCurrentTime.until(nextEventDate, MINUTES);
            StringBuilder nextEventTextBuilder = new StringBuilder()
                    .append(minsUntilEvent)
                    .append(" ")
                    .append(getResources().getString(R.string.next_event_min_to))
                    .append(" ")
                    .append(nextEvent.getName());
            mNextEventView.setText(nextEventTextBuilder);

            StringBuilder nextEventLocTextBuilder = new StringBuilder()
                    .append("at ")
                    .append(nextEvent.getLocation().getName());
            mNextEventLocView.setText(nextEventLocTextBuilder);
        }
    }

    /**
     * Refresh Itinerary RecyclerView with list of remaining events for today.
     * Called when activity starts or resumes (from editing the itinerary).
     */
    private void refreshItinerary() {
        eventsFromNow = new ArrayList<>();

        // Get remaining events for day
        for (Event e : mCurrentDayItinerary.getEvents()) {
            // Get time for mEvent's current day's instance
            LocalTime eventTime = LocalTime.of(e.getHour(), e.getMin());
            LocalDateTime eventDateTime = eventTime.atDate(mCurrentTime.toLocalDate());

            // Compare to current time, and add to mEvent set (for RecyclerView) if after
            if (eventDateTime.isAfter(mCurrentTime)) {
                eventsFromNow.add(e);
                Log.v(TAG, "Added mEvent " + e.getName());
            }
        }

        // Update adapter to plug new mEvent set to RecyclerView,
        // passing in all events for today from now
        mItineraryAdapter = new ItineraryMainAdapter(eventsFromNow);
        mItineraryRecyclerView.setAdapter(mItineraryAdapter);
        // Hide placeholder if necessary
//        if (eventsFromNow.size() > 0) {
//            TextView mPlaceholderText = findViewById(R.id.text_placeholder_itinerary);
//            if (mPlaceholderText != null)
//                ((ViewGroup) mPlaceholderText.getParent()).removeView(mPlaceholderText);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SIGNIN_REQUEST_CODE) {
            if (resultCode == SignInActivity.SIGNIN_OK) {
                assert data != null;
                String name = data.getStringExtra("name");
                mUser = new User(0, name);
                dataRepo.insertUser(mUser);
                setupActivity();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
