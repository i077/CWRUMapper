package edu.cwru.students.cwrumapper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.time.LocalTime;

public class StartTimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    /**
     * Interface that activities can implement to receive event callbacks that carry
     * the time picked.
     */
    interface StartTimePickerListener {
        void onStartTimePicked(DialogFragment dialog, int hour, int min);
    }

    StartTimePickerListener mListener;

    // Overriding to instantiate listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (StartTimePickerListener) context;
        } catch (ClassCastException e) {
            // Activity does not implement StartTimePickerListener interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement StartTimePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour, min;
        // Get event's time as time picker, otherwise get current time
        Bundle recvdArgs = getArguments();
        if (recvdArgs != null) {
            hour = recvdArgs.getInt("hour");
            min = recvdArgs.getInt("min");
        } else {
            // Get current time
            LocalTime currTime = LocalTime.now();
            hour = currTime.getHour();
            min = currTime.getMinute();
        }

        return new TimePickerDialog(getActivity(), this, hour, min,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mListener.onStartTimePicked(this, hourOfDay, minute);
    }
}
