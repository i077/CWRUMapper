package edu.cwru.students.cwrumapper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.User;

import java.time.DayOfWeek;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DayItineraryEditFragment extends Fragment {

    private static final String ARG_DAYOFWEEK = "dayofweek";
    private static final String ARG_ITINERARYID = "itineraryid";
    private int mItineraryId, mDayOfWeek;
    private User mUser;
    private OnListFragmentInteractionListener mListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayItineraryEditFragment() {
    }

    /**
     * Construct a new instance of DayItineraryEditFragment for a certain day of the week
     * @param user User from whose itineraries the fragment will read from
     * @param dayOfWeek Day of week that fragment will hold
     * @return New instance of DayItineraryEditFragment
     */
    public static DayItineraryEditFragment newInstance(User user, int itineraryId, int dayOfWeek) {
        DayItineraryEditFragment fragment = new DayItineraryEditFragment();
        fragment.mUser = user;
        Bundle args = new Bundle();
        args.putInt(ARG_DAYOFWEEK, dayOfWeek);
        args.putInt(ARG_ITINERARYID, itineraryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDayOfWeek = getArguments().getInt(ARG_DAYOFWEEK);
            mItineraryId = getArguments().getInt(ARG_ITINERARYID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dayitineraryedit_list, container, false);

        // Set day of week TextView
        TextView dayOfWeekTextView = view.findViewById(R.id.edit_list_dayofweek);
        dayOfWeekTextView.setText(DayOfWeek.of(mDayOfWeek + 1).toString());

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview_itinerary_edit);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            // Feed that day's itinerary into our RecyclerViewAdapter
            recyclerView.setAdapter(new MyDayItineraryEditRecyclerViewAdapter(
                    mUser.getItineraries().get(mItineraryId).getItinerariesForDays().get(mDayOfWeek),
                    mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event item);
    }
}
