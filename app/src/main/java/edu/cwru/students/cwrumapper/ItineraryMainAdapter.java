package edu.cwru.students.cwrumapper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Itinerary;

/**
 * Class to feed datg to itinerary recycler view in main activity.
 * This object creates ItemEventMain views for each event in the current day's itinerary
 * and recycles views when not visible.
 */
public class ItineraryMainAdapter extends RecyclerView.Adapter<ItineraryMainAdapter.ItineraryMainViewHolder> {
    private List<Event> mEventList;

    /**
     * Inner class to reference views for each event item.
     * Items are represented in the layout's root LinearLayout object.
     */
    public class ItineraryMainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;

        public ItineraryMainViewHolder(LinearLayout layout) {
            super(layout);
            mLinearLayout = layout;
        }
    }

    /**
     * Initialize the adapter with a list of events.
     * @param eventList List of events for which the adapter generates views
     */
    public ItineraryMainAdapter(List<Event> eventList) {
        mEventList = eventList;
    }

    @NonNull
    @Override
    public ItineraryMainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // TODO
        // Create a new view
        return null;
    }

    /**
     * (Re-)inflate contents of generated view for new event.
     * Used by layout manager when view becomes (nearly) visible.
     * @param holder ViewHolder to reference said view
     * @param pos New position of view in layout
     */
    @Override
    public void onBindViewHolder(@NonNull ItineraryMainViewHolder holder, int pos) {
        // TODO
    }

    /**
     * Return size of dataset. Used by the layout manager when inflating recycler view.
     * @return Size of the event list
     */
    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
