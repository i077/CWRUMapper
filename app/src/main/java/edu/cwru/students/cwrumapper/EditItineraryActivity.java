package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.time.DayOfWeek;
import java.util.Objects;

import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Repository;
import edu.cwru.students.cwrumapper.user.User;

public class EditItineraryActivity extends AppCompatActivity
        implements DayItineraryEditFragment.OnListFragmentInteractionListener {

    private Repository dataRepo;
    private User user;

    private int mDayOfWeek;

    // Pager widget to handle swiping between fragments
    private ViewPager mPager;
    // Provides pages to ViewPager
    private PagerAdapter mPagerAdapter;

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

        // Get resources
        mPager = findViewById(R.id.edit_pager);
        mPagerAdapter = new DayItineraryPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        // Start at page of current day of week by default
        mPager.setCurrentItem(mDayOfWeek - 1);

        // TODO Configure FAB to add new mEvent
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dayOfWeekSelected = mPager.getCurrentItem();
                String dayOfWeekStr = DayOfWeek.of(dayOfWeekSelected + 1).toString();
                Snackbar.make(view, "On page " + dayOfWeekStr, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
                // TODO Write edits to database
                // Write edits and go back to main activity
                finish();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onListFragmentInteraction(Event item) {
        // An event was tapped, launch EditEventActivity with event ID bundled in intent
        Snackbar.make(mPager, item.getName() + " was tapped", Snackbar.LENGTH_LONG)
                .show();
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
