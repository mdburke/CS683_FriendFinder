package com.imminentapps.friendfinder.utils;

import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

/**
 * Utility class to help out with some common user tasks
 *
 * Created by mburke on 9/28/17.
 */
public class UserUtil {
    /**
     * Load a user with profile and hobbies from separate DB calls.
     * TODO: Figure out how to get all this info into one db call
     * @param email - email of the user to load
     * @return - the user loaded with profile and hobbies
     */
    public static User loadUser(String email) {
        // Get User
        User newUser = db.userDao().findByEmail(email);

        // Get/Set Profile
        Profile userProfile = db.profileDao().findById(newUser.getId());
        newUser.setProfile(userProfile);

        // Get/Set Hobbies
        int profileId = newUser.getProfile().getProfileId();
        List<Hobby> hobbyfromdb = db.hobbyDao().getHobbyByProfileId(profileId);
        newUser.getProfile().setHobbies(hobbyfromdb);

        return newUser;
    }

    /**
     * Turn the user's list of hobbies into a string
     * @param user - the User's whose hobbies to grab
     * @return - the String version of the user's hobbies
     */
    public static String getHobbyListText(User user) {
        return hobbyListToText(user.getProfile().getHobbies()).toString();
    }

    /**
     * Convenience method for grabbing user about me text.
     * @param user - the User's whose about me text to grab
     * @return - the about me text
     */
    public static String getAboutMeText(User user) {
        String aboutMeText = user.getProfile().getAboutMeSection();
        if (aboutMeText == null) { return StringUtils.EMPTY; }
        return user.getProfile().getAboutMeSection();
    }

    /**
     * Converts a list of hobbies to a comma separated StringBuilder
     * @param hobbies - the list of hobbies to convert
     * @return - the StringBuilder comma-separated values of the hobbies
     */
    public static StringBuilder hobbyListToText(List<Hobby> hobbies) {
        StringBuilder hobbyString = new StringBuilder();

        for (int i = 0; i < hobbies.size(); i++) {
            if (i != 0) {
                hobbyString.append(", ");
            }
            hobbyString.append(hobbies.get(i).getHobby());
        }

        return hobbyString;
    }

    /**
     * Converts a comma separated string of hobbies into an array of hobbies
     * @param profileId - the profile ID to assign to the hobbies in the DB
     * @param hobbies - the String of the comma separated hobbies
     * @return an array of hobbies
     */
    public static Hobby[] textToHobbyArray(int profileId, String hobbies) {
        String[] hobbyStringArray = StringUtils.split(hobbies, ", ");
        Hobby[] hobbyArray = new Hobby[hobbyStringArray.length];

        for (int i = 0; i < hobbyArray.length; i++) {
            hobbyArray[i] = new Hobby(profileId, hobbyStringArray[i]);
        }

        return hobbyArray;
    }
}
