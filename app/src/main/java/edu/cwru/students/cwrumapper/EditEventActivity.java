package edu.cwru.students.cwrumapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

public class EditEventActivity extends AppCompatActivity {

    private int mEventId;
    private int mDayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mEventId = intent.getIntExtra("eventId", -1);
        mDayOfWeek = intent.getIntExtra("dayOfWeek", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_event, menu);
        return true;
    }
}
