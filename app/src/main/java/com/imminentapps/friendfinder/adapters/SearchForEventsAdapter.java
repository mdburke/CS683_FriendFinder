package com.imminentapps.friendfinder.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.Event;
import com.imminentapps.friendfinder.interfaces.ActivityCommunication;
import com.imminentapps.friendfinder.utils.EventUtil;

import java.util.List;

/**
 * Created by mburke on 10/1/17.
 */

public class SearchForEventsAdapter extends RecyclerView.Adapter<SearchForEventsAdapter.EventsViewHolder>  {

    List<Event> events;
    Context context;
    ActivityCommunication activityCommunication;

    public SearchForEventsAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;

        if (context instanceof ActivityCommunication) {
            activityCommunication = (ActivityCommunication) context;
        }
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(context).inflate(R.layout.custom_seach_for_friends_layout, parent, false);

        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchForEventsAdapter.EventsViewHolder holder, int position) {
        Event event = EventUtil.loadEvent(events.get(position).getEventId());

        holder.date.setText(event.getDate().toString());
        holder.title.setText(event.getTitle());

        // Add the onClickListener
        holder.cardView.setOnClickListener(view -> activityCommunication.userClicked(String.valueOf(event.getEventId())));
    }

    @Override
    public int getItemCount() {
        return events.size();
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
