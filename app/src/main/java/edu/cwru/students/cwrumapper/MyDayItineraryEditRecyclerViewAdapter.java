package edu.cwru.students.cwrumapper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.cwru.students.cwrumapper.DayItineraryEditFragment.OnListFragmentInteractionListener;
import edu.cwru.students.cwrumapper.user.DayItinerary;
import edu.cwru.students.cwrumapper.user.Event;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that displays a DayItinerary and makes a call to the
 * specified {@link OnListFragmentInteractionListener}, editing the selected event.
 */
public class MyDayItineraryEditRecyclerViewAdapter extends RecyclerView.Adapter<MyDayItineraryEditRecyclerViewAdapter.ViewHolder> {

    private final DayItinerary mDayItinerary;
    private final OnListFragmentInteractionListener mListener;

    private List<Event> mEvents;

    public MyDayItineraryEditRecyclerViewAdapter(DayItinerary dayItinerary, OnListFragmentInteractionListener listener) {
        mDayItinerary = dayItinerary;
        mEvents = dayItinerary.getEvents();
        mListener = listener;
    }

    /**
     * Create new view for events.
     * Used by the layout manager when new views need to be made (not enough exist to hold events on screen)
     * @param parent The parent view
     * @param viewType
     * @return A new ViewHolder holding what will be mEvent information
     */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dayitineraryedit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mEvent = mEvents.get(position);
        // Fill text views
        holder.mEventNameView.setText(mEvents.get(position).getName());
        holder.mEventLocView.setText(mEvents.get(position).getLocation().getName());

        // Add leading zeros if necessary
        String properStartHour = String.format(Locale.getDefault(), "%02d", holder.mEvent.getHour());
        String properStartMin = String.format(Locale.getDefault(), "%02d", holder.mEvent.getMin());
        String properEndHour = String.format(Locale.getDefault(), "%02d", holder.mEvent.getEndHour());
        String properEndMin = String.format(Locale.getDefault(), "%02d", holder.mEvent.getEndMin());

        String startTimeStr = properStartHour + ":" + properStartMin;
        holder.mEventStartTimeView.setText(startTimeStr);
        String endTimeStr = properEndHour + ":" + properEndMin;
        holder.mEventEndTimeView.setText(endTimeStr);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mEvent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    /**
     * Inner class to represent views for each Event item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEventNameView;
        public final TextView mEventLocView;
        public final TextView mEventStartTimeView, mEventEndTimeView;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mEventNameView = view.findViewById(R.id.edit_list_event_name);
            mEventLocView = view.findViewById(R.id.edit_list_event_location);
            mEventStartTimeView = view.findViewById(R.id.edit_list_event_start_time);
            mEventEndTimeView = view.findViewById(R.id.edit_list_event_end_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEventNameView.getText() + "'";
        }
    }
}
