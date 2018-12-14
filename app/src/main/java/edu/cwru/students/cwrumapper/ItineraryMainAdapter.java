package edu.cwru.students.cwrumapper;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Itinerary;

/**
 * Class to feed data to itinerary recycler view in main activity.
 * This object creates ItemEventMain views for each mEvent in the current day's itinerary
 * and recycles views when not visible.
 */
public class ItineraryMainAdapter extends RecyclerView.Adapter<ItineraryMainAdapter.ItineraryMainViewHolder> {
    private List<Event> mEventList;

    /**
     * Inner class to reference views for each mEvent item.
     * Items are represented in the layout's root LinearLayout object.
     */
    public class ItineraryMainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView mStartTimeText, mEndTimeText, mEventNameText, mEventLocText;

        public ItineraryMainViewHolder(LinearLayout layout) {
            super(layout);
            // Build references to layout children to inflate in adapter
            mLinearLayout = layout;
            mStartTimeText = layout.findViewById(R.id.event_start_time);
            mEndTimeText = layout.findViewById(R.id.event_end_time);
            mEventNameText = layout.findViewById(R.id.event_name);
            mEventLocText = layout.findViewById(R.id.event_location);
        }
    }

    /**
     * Initialize the adapter with a list of events.
     * @param eventList List of events for which the adapter generates views
     */
    public ItineraryMainAdapter(List<Event> eventList) {
        mEventList = eventList;
    }

    /**
     * Create new view for events.
     * Used by the layout manager when not enough view holders exist to hold events on screen.
     * @param parent The parent view
     * @param viewType
     * @return A new ViewHolder holding what will be mEvent information
     */
    @NonNull
    @Override
    public ItineraryMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_main, parent, false);
        return new ItineraryMainViewHolder(linearLayout);
    }

    /**
     * (Re-)inflate contents of generated view with new mEvent data.
     * Used by layout manager when view becomes (nearly) visible.
     * @param holder ViewHolder to reference said view
     * @param pos New position of view in layout
     */
    @Override
    public void onBindViewHolder(@NonNull ItineraryMainViewHolder holder, int pos) {
        Event newEvent = mEventList.get(pos);

        // Add leading zeros if necessary
        String properStartHour = String.format(Locale.getDefault(), "%02d", newEvent.getHour());
        String properStartMin = String.format(Locale.getDefault(), "%02d", newEvent.getMin());
        String properEndHour = String.format(Locale.getDefault(), "%02d", newEvent.getEndHour());
        String properEndMin = String.format(Locale.getDefault(), "%02d", newEvent.getEndMin());

        // Fill layout children
        String startTimeStr = properStartHour + ":" + properStartMin;
        holder.mStartTimeText.setText(startTimeStr);
        String endTimeStr = properEndHour + ":" + properEndMin;
        holder.mEndTimeText.setText(endTimeStr);
        holder.mEventNameText.setText(newEvent.getName());
        StringBuilder eventLocTextBuilder = new StringBuilder()
                .append(newEvent.getLocation().getName());
        if (!newEvent.getRoomNumber().equals("0")) {
            eventLocTextBuilder.append(" ")
                .append(newEvent.getRoomNumber());
        }
        holder.mEventLocText.setText(eventLocTextBuilder);
    }

    /**
     * Return size of dataset. Used by the layout manager when inflating recycler view.
     * @return Size of the mEvent list
     */
    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
