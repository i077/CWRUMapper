package edu.cwru.students.cwrumapper;


import android.arch.persistence.room.Room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.signin.internal.SignInClientImpl;

import edu.cwru.students.cwrumapper.user.UserDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
    }

}
