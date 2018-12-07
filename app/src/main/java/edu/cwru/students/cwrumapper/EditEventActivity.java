package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.stream.Stream;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Location;
import edu.cwru.students.cwrumapper.user.Repository;

public class EditEventActivity extends AppCompatActivity implements
        DayChooserDialogFragment.DayChooserDialogListener,
        StartTimePickerFragment.StartTimePickerListener,
        LengthPickerFragment.LengthPickerListener {

    public static final int EVENT_CANCELLED = 0,
            EVENT_MODIFIED = 1,
            EVENT_DELETED = 2;
    private static final String TAG = "EditEventActivity";

    private Event mEventRecvd;

    // Fields that will be parcelized to new Event
    private int mDayItineraryNum;
    private String mEventNewName, mEventNewRoomNumber;
    private int mEventNewHour, mEventNewMin, mEventNewLength;
    private String mEventNewLocationName;

    private Repository dataRepo;

    // Fields to keep track of layouts
    private TextView mEventNameView;
    private LinearLayout mEventDayItinView;
    private TextView mEventDayItinText;
    private LinearLayout mEventStartTimeView;
    private TextView mEventStartTimeText;
    private LinearLayout mEventLengthView;
    private TextView mEventLengthText;
    private LinearLayout mEventLocationView;
    private AutoCompleteTextView mEventLocationText;
    private TextView mEventRoomNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataRepo = new Repository(getApplicationContext());

        Intent intent = getIntent();
        // Read event. If this is null, we're creating a new one
        mEventRecvd = intent.getParcelableExtra("event");
        mDayItineraryNum = intent.getIntExtra("dayItineraryNum", -1);
        Log.d(TAG, "Read in day " + mDayItineraryNum);

        // Initialize layout views
        mEventNameView = findViewById(R.id.edit_event_name);
        mEventDayItinView = findViewById(R.id.edit_event_dayitinerary_layout);
        mEventDayItinText = findViewById(R.id.edit_event_dayitinerary_text);
        mEventStartTimeText = findViewById(R.id.edit_event_start_time);
        mEventStartTimeView = findViewById(R.id.edit_event_start_layout);
        mEventLengthView = findViewById(R.id.edit_event_length_layout);
        mEventLengthText = findViewById(R.id.edit_event_length);
        mEventLocationView = findViewById(R.id.edit_event_location_layout);
        mEventLocationText = findViewById(R.id.edit_event_location);
        mEventRoomNumberText = findViewById(R.id.edit_event_roomnumber);

        // Inflate views and fields with event content if needed
        if (mEventRecvd != null) {
            mEventNameView.setText(mEventRecvd.getName());
            mEventNewName = mEventRecvd.getName();

            mEventDayItinText.setText(DayItinerary.intToWeekday(mDayItineraryNum));
            mEventDayItinText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            mEventNewHour = mEventRecvd.getHour();
            mEventNewMin = mEventRecvd.getMin();
            String properStartHour = String.format(Locale.getDefault(), "%02d", mEventNewHour);
            String properStartMin = String.format(Locale.getDefault(), "%02d", mEventNewMin);
            String startTimeStr = properStartHour + ":" + properStartMin;
            mEventStartTimeText.setText(startTimeStr);
            mEventStartTimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            mEventNewLength = mEventRecvd.getLength();
            String lengthStr = mEventNewLength + " " + getResources().getString(R.string.event_length_suffix);
            mEventLengthText.setText(lengthStr);
            mEventLengthText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            mEventNewLocationName = mEventRecvd.getLocation().getName();
            mEventLocationText.setText(mEventNewLocationName);
            mEventLocationText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            mEventNewRoomNumber = mEventRecvd.getRoomNumber();
            mEventRoomNumberText.setText(mEventNewRoomNumber);
        }

        // Set up DayItinerary chooser dialog
        mEventDayItinView.setOnClickListener(v -> {
            DayChooserDialogFragment dialogFragment = new DayChooserDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "dayitinchooser");
        });

        // Set up StartTimePicker dialog
        Bundle startTimePickerArgs = new Bundle();
        if (mEventRecvd != null) {
            startTimePickerArgs.putInt("hour", mEventRecvd.getHour());
            startTimePickerArgs.putInt("min", mEventRecvd.getMin());
        }

        mEventStartTimeView.setOnClickListener(v -> {
            // Bundle event start and end times
            StartTimePickerFragment pickerFragment = new StartTimePickerFragment();
            if (mEventRecvd != null)
                pickerFragment.setArguments(startTimePickerArgs);
            pickerFragment.show(getSupportFragmentManager(), "starttimepicker");
        });

        // Set up LengthPicker dialog
        Bundle lengthPickerArgs = new Bundle();
        if (mEventRecvd != null) {
            lengthPickerArgs.putInt("length", mEventRecvd.getLength());
        }

        mEventLengthView.setOnClickListener(v -> {
            // Bundle event length
            LengthPickerFragment pickerFragment = new LengthPickerFragment();
            if (mEventRecvd != null)
                pickerFragment.setArguments(lengthPickerArgs);
            pickerFragment.show(getSupportFragmentManager(), "lengthpicker");
        });

        // Set up Location autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, Location.getLocationNames());
        mEventLocationText.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_delete:
                setResult(EVENT_DELETED);
                finish();
                break;
            case R.id.action_done:
                // Validate first, and alert user if location name is invalid
                if (!updateNewEventFields()) {
                    View thisView = findViewById(android.R.id.content);
                    Snackbar.make(thisView, R.string.errormsg_locationedit_invalid,
                            Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                } else {
                    intent.putExtra("newEvent", constructNewEvent());
                    intent.putExtra("newDayItinerary", mDayItineraryNum);
                    setResult(EVENT_MODIFIED, intent);
                    finish();
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        setResult(EVENT_CANCELLED);
        super.onBackPressed();
    }

    /**
     * DayChooserDialog was dismissed with an item selected, so update day itinerary number
     * @param dialog Dialog that was just dismissed
     * @param which Index of DayItinerary that was picked
     */
    @Override
    public void onDayChooserDialogItemClick(DialogFragment dialog, int which) {
        mDayItineraryNum = which;
        mEventDayItinText.setText(DayItinerary.intToWeekday(mDayItineraryNum));
        mEventDayItinText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
    }

    /**
     * StartTimePicker was dismissed with time picked, so update time
     * @param dialog Dialog that was just dismissed
     * @param hour Hour of day that was picked
     * @param min Minute of hour that was picked
     */
    @Override
    public void onStartTimePicked(DialogFragment dialog, int hour, int min) {
        mEventNewHour = hour;
        mEventNewMin = min;
        String properStartHour = String.format(Locale.getDefault(), "%02d", mEventNewHour);
        String properStartMin = String.format(Locale.getDefault(), "%02d", mEventNewMin);
        String startTimeStr = properStartHour + ":" + properStartMin;
        mEventStartTimeText.setText(startTimeStr);
        mEventStartTimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
    }

    @Override
    public void onLengthPicked(DialogFragment dialog, int length) {
        mEventNewLength = length;
        String lengthStr = mEventNewLength + " " + getResources().getString(R.string.event_length_suffix);
        mEventLengthText.setText(lengthStr);
        mEventLengthText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
    }

    private boolean updateNewEventFields() {
        mEventNewName = mEventNameView.getText().toString();
        mEventNewLocationName = mEventLocationText.getText().toString();
        // Validate location name
        if (Stream.of(Location.getLocationNames()).noneMatch(n -> n.equals(mEventNewLocationName))) {
            return false;
        }
        mEventNewRoomNumber = mEventRoomNumberText.getText().toString();
        return true;
    }

    private Event constructNewEvent() {
        Location newLocation = Location.getLocationByName(mEventNewLocationName);
        return new Event(mEventNewName, newLocation, mEventNewLength, mEventNewRoomNumber, mEventNewHour, mEventNewMin, 0);
    }
}
