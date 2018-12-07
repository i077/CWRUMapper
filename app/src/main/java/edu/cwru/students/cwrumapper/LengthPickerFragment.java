package edu.cwru.students.cwrumapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Objects;

public class LengthPickerFragment extends DialogFragment {

    /**
     * Interface that activities can implement to receive event callbacks that carry
     * the length picked.
     */
    public interface LengthPickerListener {
        void onLengthPicked(DialogFragment dialog, int length);
    }

    LengthPickerListener mListener;

    // Overriding to instantiate listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (LengthPickerListener) context;
        } catch (ClassCastException e) {
            // Activity does not implement LengthPickerListener, throw exception
            throw new ClassCastException(getActivity().toString()
                    + "must implement LengthPickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int oldLength;

        View v = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.fragment_length_picker, null);
        NumberPicker numberPicker = v.findViewById(R.id.length_number_picker);

        numberPicker.setMaxValue(60*24);
        numberPicker.setMinValue(1);

        Bundle recvdArgs = getArguments();
        if (recvdArgs != null) {
            oldLength = recvdArgs.getInt("length");
        } else {
            oldLength = 50;
        }

        numberPicker.setValue(oldLength);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.edit_event_length_default)
                .setView(v)
                .setPositiveButton(R.string.action_ok, (dialog, which) -> {
                    mListener.onLengthPicked(this, numberPicker.getValue());
                    dismiss();
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dismiss());

        return builder.create();
    }
}
