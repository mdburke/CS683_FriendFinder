package com.imminentapps.friendfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.adapters.UserSearchResultViewAdapter;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchResultFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<User> searchResults;
    private RecyclerView recyclerView;
    private UserSearchResultViewAdapter searchResultViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchResultFragment() {}

    @SuppressWarnings("unused")
    public static SearchResultFragment newInstance(int columnCount) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        searchResults = new ArrayList<>();
        searchResultViewAdapter = new UserSearchResultViewAdapter(getSearchResults(), mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_searchresult_list, container, false);

        // Set the adapter
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerView.setAdapter(searchResultViewAdapter);
        return recyclerView;
    }

    private List<User> getSearchResults() {
        // TODO: Switch to filtering in the DB Query
        searchResults.addAll(db.userDao().getAll());
        for (User user : searchResults) {
            Profile profile = db.profileDao().findById(user.getId());
            user.setProfile(profile);
        }

        return searchResults;
    }

    public void filterSearchResults(String filter) {
        switch (filter) {
            case Constants.SEARCH_FILTER_ALL_USERS:
                break;
            case Constants.SEARCH_FILTER_FRIENDS:
                break;
            case Constants.SEARCH_FILTER_NOT_FRIENDS:
                searchResults.remove(0);
                break;
        }
        searchResultViewAdapter.notifyDataSetChanged();
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
        void onListFragmentInteraction(User user);
    }
}
