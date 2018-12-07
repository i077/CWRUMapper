package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.time.DayOfWeek;
import java.util.Objects;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Repository;
import edu.cwru.students.cwrumapper.user.User;

public class EditItineraryActivity extends AppCompatActivity
        implements DayItineraryEditFragment.OnListFragmentInteractionListener {

    private static final String TAG = "EditItineraryActivity";

    private Repository dataRepo;
    private User user;

    private int mDayOfWeek;
    private Event mEventSelected;
    private int mDayOfWeekSelected;

    // Pager widget to handle swiping between fragments
    private ViewPager mPager;
    // Provides pages to ViewPager
    private PagerAdapter mPagerAdapter;

    private static final int EDIT_EVENT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent recvIntent = getIntent();
        mDayOfWeek = recvIntent.getIntExtra("dayOfWeek", 1);

        // Initialize data repository
        dataRepo = new Repository(getApplication());
        int userID = recvIntent.getIntExtra("userID", 0);
        user = dataRepo.getUser(userID);
        if (user == null)
            user = dataRepo.getUser(0);

        // Get resources
        mPager = findViewById(R.id.edit_pager);
        mPagerAdapter = new DayItineraryPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        // Start at page of current day of week by default
        mPager.setCurrentItem(mDayOfWeek - 1);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dayOfWeekSelected = mPager.getCurrentItem();
                Intent intent = new Intent(EditItineraryActivity.this, EditEventActivity.class);
                intent.putExtra("event", (Event) null);
                intent.putExtra("dayItineraryNum", dayOfWeekSelected);
                EditItineraryActivity.this.startActivityForResult(intent, EDIT_EVENT_ACTIVITY_REQUEST_CODE);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        refreshItinerary();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_itinerary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                dataRepo.insertUser(user);
                finish();
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Called when a ViewHolder item in one of ViewPager's RecyclerViews (ie. an Event) is tapped.
     * This method will bundle the tapped event into an Intent to be sent to EditEventActivity.
     * @param item Event in ViewHolder that was tapped
     */
    @Override
    public void onListFragmentInteraction(Event item) {
        // An event was tapped, launch EditEventActivity with event bundled in intent
        mEventSelected = item;
        Intent intent = new Intent(this, EditEventActivity.class);
        mDayOfWeekSelected = mPager.getCurrentItem();
        intent.putExtra("dayItineraryNum", mDayOfWeekSelected);
        intent.putExtra("event", item);
        startActivityForResult(intent, EDIT_EVENT_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Take action on the user structure depending on what EditEventActivity returns in its result code
     * and intent.
     * @param requestCode The request code matching the activity's request code
     * @param resultCode The result code returned from the child activity
     * @param data Intent data, possibly containing an event
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_EVENT_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case EditEventActivity.EVENT_DELETED:
                    DayItinerary dayItinerarySelected = user.getItineraries().get(0).getItinerariesForDays()
                            .get(mDayOfWeekSelected);
                    dayItinerarySelected.deleteEvent(mEventSelected);
                    Log.d(TAG, "DayItin " + dayItinerarySelected.getId() + " now has " + dayItinerarySelected.getEvents().size() + " events");
                    break;
            }
        }
    }

    private void refreshItinerary() {
        mPagerAdapter = new DayItineraryPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        // Start at page of current day of week by default
        mPager.setCurrentItem(mDayOfWeek - 1);
    }

    /**
     * Pager adapter to provide DayItineraryEditFragment objects in sequence
     */
    private class DayItineraryPagerAdapter extends FragmentStatePagerAdapter {
        public DayItineraryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            // Pass in the default itinerary (0) for now
            return DayItineraryEditFragment.newInstance(user, 0, i);
        }

        @Override
        public int getCount() {
            // Returns amount of DayItineraries in user's Itinerary
            return user.getItineraries().get(0).getItinerariesForDays().size();
        }
    }
}
