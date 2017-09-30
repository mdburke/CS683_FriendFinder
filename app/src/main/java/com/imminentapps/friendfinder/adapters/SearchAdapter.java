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
 * Created by mburke on 9/30/17.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder> {

    List<User> users;
    Context context;
    boolean onClickReceived = false;
    UpdateMainClass updateMainClass;

    public SearchAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

        if (context instanceof UpdateMainClass) {
            updateMainClass = (UpdateMainClass) context;
        }
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(context).inflate(R.layout.custom_searchview_layout, parent, false);

        return new SearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchItemViewHolder holder, int position) {
        User user = UserUtil.loadUser(users.get(position).getEmail());
        holder.username.setText(user.getProfile().getUsername());
        holder.email.setText(user.getEmail());
        holder.cardView.setOnClickListener(view -> updateMainClass.userClicked(user.getEmail()));
        if (!updateMainClass.getCurrentUser().isFriendsWith(user.getId(), context)) {
            holder.friendIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

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

    public interface UpdateMainClass {
        void userClicked(String email);
        User getCurrentUser();
    }
}
