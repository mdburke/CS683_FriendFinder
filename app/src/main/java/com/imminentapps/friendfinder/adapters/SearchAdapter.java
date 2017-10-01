package com.imminentapps.friendfinder.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.UserUtil;

import java.util.List;

/**
 * Custom Adapter class for the Search Screen data
 * Created by mburke on 9/30/17.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder> {

    List<User> users;
    Context context;
    // Use this to access the activity
    ActivityCommunication activityCommunication;

    public SearchAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

        if (context instanceof ActivityCommunication) {
            activityCommunication = (ActivityCommunication) context;
        }
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(context).inflate(R.layout.custom_searchview_layout, parent, false);

        return new SearchItemViewHolder(view);
    }

    /**
     * Setup each CardView for the RecyclerView list
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final SearchItemViewHolder holder, int position) {
        // Grab the user
        User user = UserUtil.loadUser(users.get(position).getEmail());

        // Set the fields
        holder.username.setText(user.getProfile().getUsername());
        holder.email.setText(user.getEmail());

        // Add the onClickListener
        holder.cardView.setOnClickListener(view -> activityCommunication.userClicked(user.getEmail()));

        // If user is not a friend to the current user, make the friend icon invisible
        if (!activityCommunication.getCurrentUser().isFriendsWith(user.getId(), context)) {
            holder.friendIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Custom ViewHolder class for the RecyclerView
     */
    public class SearchItemViewHolder  extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView email;
        private CardView cardView;
        private ImageView friendIcon;

        public SearchItemViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.card_username);
            email = itemView.findViewById(R.id.card_email);
            cardView = itemView.findViewById(R.id.card_holder);
            friendIcon = itemView.findViewById(R.id.searchScreen_friendIcon);
        }
    }

    /**
     * Interface for communicating with the activity
     */
    public interface ActivityCommunication {
        void userClicked(String email);
        User getCurrentUser();
    }
}
