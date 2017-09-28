package com.imminentapps.friendfinder.utils;

import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

/**
 * Created by mburke on 9/28/17.
 */
public class UserUtil {
    public static User loadUser(String email) {
        // TODO: Figure out how to get all this info into one db call
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

    public static String getHobbyListText(User user) {
        return hobbyListToText(user.getProfile().getHobbies()).toString();
    }

    public static String getAboutMeText(User user) {
        String aboutMeText = user.getProfile().getAboutMeSection();
        if (aboutMeText == null) { return StringUtils.EMPTY; }
        return user.getProfile().getAboutMeSection();
    }

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

    public static Hobby[] textToHobbyArray(int profileId, String hobbies) {
        String[] hobbyStringArray = StringUtils.split(hobbies, ", ");
        Hobby[] hobbyArray = new Hobby[hobbyStringArray.length];

        for (int i = 0; i < hobbyArray.length; i++) {
            hobbyArray[i] = new Hobby(profileId, hobbyStringArray[i]);
        }

        return hobbyArray;
    }
}
