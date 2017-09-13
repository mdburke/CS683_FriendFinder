package com.imminentapps.friendfinder.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold user profile information
 *
 * Created by mburke on 9/12/17.
 */

public class Profile {
    private List<String> hobbies;
    private String username;
    private String aboutMeSection;
    private String firstName;
    private String lastName;

    public Profile(List<String> hobbies, String username, String aboutMeSection,
                   String firstName, String lastName) {
        this.hobbies = (hobbies != null) ? hobbies : new ArrayList<>();
        this.username = username;
        this.aboutMeSection = aboutMeSection;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Profile(List<String> hobbies, String username, String aboutMeSection) {
        this.hobbies = (hobbies != null) ? hobbies : new ArrayList<>();
        this.username = username;
        this.aboutMeSection = aboutMeSection;
    }

    public Profile() {}

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAboutMeSection() {
        return aboutMeSection;
    }

    public void setAboutMeSection(String aboutMeSection) {
        this.aboutMeSection = aboutMeSection;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (hobbies != null ? !hobbies.equals(profile.hobbies) : profile.hobbies != null)
            return false;
        if (username != null ? !username.equals(profile.username) : profile.username != null)
            return false;
        return aboutMeSection != null ? aboutMeSection.equals(profile.aboutMeSection) : profile.aboutMeSection == null;

    }

    @Override
    public int hashCode() {
        int result = hobbies != null ? hobbies.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (aboutMeSection != null ? aboutMeSection.hashCode() : 0);
        return result;
    }
}
