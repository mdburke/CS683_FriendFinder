package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mburke on 9/21/17.
 */
@Entity
public class Hobby {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "profile_id")
    private int profileId;

    private String hobby;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
