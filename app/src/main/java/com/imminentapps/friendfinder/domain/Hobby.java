package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * POJO for holding Hobby information.
 * Created by mburke on 9/21/17.
 */
@Entity(foreignKeys = @ForeignKey(entity = Profile.class, parentColumns = "profile_id", childColumns = "profile_id"),
primaryKeys = {"profile_id", "hobby"})
public class Hobby {
    @ColumnInfo(name = "profile_id")
    private int profileId;

    private String hobby;

    public Hobby(int profileId, String hobby) {
        this.profileId = profileId;
        this.hobby = hobby;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}
