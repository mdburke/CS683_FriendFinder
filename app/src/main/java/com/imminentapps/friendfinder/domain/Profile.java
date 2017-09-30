package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * POJO to hold user profile information
 * Created by mburke on 9/12/17.
 */
@Entity(foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "profile_id",
        onDelete = CASCADE))
public class Profile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    private int profileId;

    @ColumnInfo(name = "about_me_section")
    private String aboutMeSection;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "profile_image_uri")
    private String profileImageUri;

    @Ignore
    private List<Hobby> hobbies;

    private String username;

    @Ignore
    public Profile(List<Hobby> hobbies, String username, String aboutMeSection,
                   String firstName, String lastName, String profileImageUri) {
        this.hobbies = (hobbies != null) ? hobbies : new ArrayList<>();
        this.username = username;
        this.aboutMeSection = aboutMeSection;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUri = profileImageUri;
    }

    @Ignore
    public Profile(List<Hobby> hobbies, String username, String aboutMeSection) {
        this.hobbies = (hobbies != null) ? hobbies : new ArrayList<>();
        this.username = username;
        this.aboutMeSection = aboutMeSection;
    }

    public Profile() {}

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public List<String> getHobbiesAsStrings() {
        List<String> hobbiesString = new ArrayList<>();
        for (Hobby hobby: hobbies) {
            hobbiesString.add(hobby.getHobby());
        }
        return hobbiesString;
    }
    public void setHobbies(List<Hobby> hobbies) {
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

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImage) {
        this.profileImageUri = profileImage;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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
