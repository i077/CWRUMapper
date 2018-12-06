package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.cwru.students.cwrumapper.user.Event;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int EVENT_CANCELLED = 0,
            EVENT_MODIFIED = 1,
            EVENT_DELETED = 2;

    private Event mEventRecvd;
    private int mDayOfWeek;
    private int mResult;

    private TextView mEventNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        // Read event. If this is null, we're creating a new one
        mEventRecvd = intent.getParcelableExtra("event");
        mDayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        // Initialize layout views
        mEventNameView = findViewById(R.id.edit_event_name);

        // Inflate views with event content if needed
        if (mEventRecvd != null) {
            mEventNameView.setText(mEventRecvd.getName());
        }
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

    // TODO Use intents to send Event back to parent when finished
    @Override
    public void onClick(View v) {

    }

}
