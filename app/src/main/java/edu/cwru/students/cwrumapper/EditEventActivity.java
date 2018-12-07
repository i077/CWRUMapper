package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Repository;

public class EditEventActivity extends AppCompatActivity implements
        DayChooserDialogFragment.DayChooserDialogListener {

    public static final int EVENT_CANCELLED = 0,
            EVENT_MODIFIED = 1,
            EVENT_DELETED = 2;
    private static final String TAG = "EditEventActivity";

    private Event mEventRecvd;
    private int mDayItineraryNum;
    private int mResult;

    private Repository dataRepo;

    private TextView mEventNameView;
    private LinearLayout mEventDayItinView;
    private TextView mEventDayItinText;

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

        // Inflate views with event content if needed
        if (mEventRecvd != null) {
            mEventNameView.setText(mEventRecvd.getName());
            mEventDayItinText.setText(DayItinerary.intToWeekday(mDayItineraryNum));
            mEventDayItinText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }

        mEventDayItinView.setOnClickListener(v -> {
            Log.v(TAG, "Showing day itinerary dialog");
            DayChooserDialogFragment dialogFragment = new DayChooserDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "dayitinchooser");
        });
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
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        setResult(EVENT_CANCELLED);
        super.onBackPressed();
    }

    @Override
    public void onDialogItemClick(DialogFragment dialog, int which) {
        mDayItineraryNum = which;
        mEventDayItinText.setText(DayItinerary.intToWeekday(mDayItineraryNum));
    }
}
