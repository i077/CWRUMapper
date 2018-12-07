package edu.cwru.students.cwrumapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

        import java.util.ArrayList;
        import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Itinerary;
import edu.cwru.students.cwrumapper.user.Repository;
import edu.cwru.students.cwrumapper.user.User;

public class DayChooserDialogFragment extends DialogFragment {

    /**
     * Interface that activities can implement to receive event callbacks that carry
     * which item was selected, if any.
     */
    interface DayChooserDialogListener {
        void onDialogItemClick(DialogFragment dialog, int which);
    }

    DayChooserDialogListener mListener;

    // Overriding to instantiate listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DayChooserDialogListener) context;
        } catch (ClassCastException e) {
            // Activity does not implement DayChooserDialogListener interface, throw exception
            throw new ClassCastException(getActivity().toString()
                + " must implement DayChooserDialogListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Repository dataRepo = new Repository(getContext());
        // Getting default user!
        User mUser = dataRepo.getUser(0);
        Itinerary mItinerary = mUser.getItineraries().get(0);
        List<DayItinerary> dayItineraries = mItinerary.getItinerariesForDays();

        List<String> dayItinNames = new ArrayList<>();
        for (int i = 0; i < dayItineraries.size(); i++) {
            dayItinNames.add(DayItinerary.intToWeekday(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1, dayItinNames);

        // Construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.edit_event_dayitin_default)
                .setAdapter(adapter, (dialog, which) -> {
                    mListener.onDialogItemClick(this, which);
                    dismiss();
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dismiss());
        return builder.create();
    }
}
