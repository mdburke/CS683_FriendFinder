package com.imminentapps.friendfinder.interfaces;

import com.imminentapps.friendfinder.domain.User;

/**
 * Created by mburke on 10/1/17.
 */
public interface ActivityCommunication {
        void userClicked(String email);
        User getCurrentUser();
}
