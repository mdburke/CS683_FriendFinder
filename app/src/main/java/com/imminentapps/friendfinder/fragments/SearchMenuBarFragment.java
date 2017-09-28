package com.imminentapps.friendfinder.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mburke on 9/13/17.
 */
public class SearchMenuBarFragment extends Fragment {
    private View rootView;
    private Spinner friendSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_menubar, container, false);
        friendSpinner = rootView.findViewById(R.id.searchMenu_friendSpinner);

        List<String> choiceList = new ArrayList<>();
        choiceList.add(Constants.SEARCH_FILTER_ALL_USERS);
        choiceList.add(Constants.SEARCH_FILTER_FRIENDS);
        choiceList.add(Constants.SEARCH_FILTER_NOT_FRIENDS);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, choiceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendSpinner.setAdapter(adapter);

        return rootView;
    }
}
