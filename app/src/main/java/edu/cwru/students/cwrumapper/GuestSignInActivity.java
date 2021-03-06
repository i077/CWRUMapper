package edu.cwru.students.cwrumapper;



import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Calendar;

public class GuestSignInActivity extends AppCompatActivity {

    TextView dateView;
    Button start_date_button;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialog2;
    TextView endDateView;
    Button end_date_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_in);

        dateView = (TextView)findViewById(R.id.start_date_text);
        start_date_button = (Button)findViewById(R.id.start_date_button);
        start_date_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(GuestSignInActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        dateView.setText((mMonth + 1) + "/" + mDay + "/" + mYear);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        endDateView = (TextView)findViewById(R.id.end_date_text);
        end_date_button = (Button)findViewById(R.id.end_date_button);
        end_date_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog2 = new DatePickerDialog(GuestSignInActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        endDateView.setText((mMonth+1) + "/" + mDay + "/" + mYear);
                    }
                }, year, month, day);
                datePickerDialog2.show();
            }
        });

    }
}
