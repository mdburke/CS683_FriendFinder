package com.imminentapps.friendfinder.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.Event;
import com.imminentapps.friendfinder.interfaces.ActivityCommunication;
import com.imminentapps.friendfinder.utils.EventUtil;

import java.util.List;

/**
 * Custom RecyclerView Adapter class for the SearchForFriendsScreen. It converts
 * a List of Events into the RecyclerView.
 *
 * Created by mburke on 10/1/17.
 */
public class SearchForEventsAdapter extends RecyclerView.Adapter<SearchForEventsAdapter.EventsViewHolder>  {

    // Object used for communicating back to the parent activity. See interface for details.
    private ActivityCommunication activityCommunication;
    private List<Event> events;
    private Context context;

    public SearchForEventsAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;

        if (context instanceof ActivityCommunication) {
            activityCommunication = (ActivityCommunication) context;
        }
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view and return it
        View view = LayoutInflater.from(context).inflate(R.layout.custom_seach_for_events_layout, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    @Override
    @SuppressWarnings("Convert2Lambda")
    public void onBindViewHolder(SearchForEventsAdapter.EventsViewHolder holder, int position) {
        DatabaseTask<Integer, Event> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<Event>() {
            @Override
            public void onFinished(Event event) {
                // Set the fields of the CardView in the RecyclerView
                holder.date.setText(event.getDate().toString());
                holder.title.setText(event.getTitle());

                // Add the onClickListener
                holder.cardView.setOnClickListener(view -> activityCommunication.userClicked(String.valueOf(event.getEventId())));
            }
        }, new DatabaseTask.DatabaseTaskQuery<Integer, Event>() {
            @Override
            public Event execute(Integer... position) {
                return EventUtil.loadEvent(events.get(position[0]).getEventId());
            }
        });
        task.execute(position);
    }

    /**
     * Custom ViewHolder class for the RecyclerView
     */
    public class EventsViewHolder  extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private CardView cardView;

        public EventsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_eventTitle);
            date = itemView.findViewById(R.id.search_eventDate);
            cardView = itemView.findViewById(R.id.search_for_events_card);
        }
    }
}
