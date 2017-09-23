package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by mburke on 9/21/17.
 */
@Entity(tableName = "user_relationship",
        foreignKeys = @ForeignKey(entity = RelationshipType.class, parentColumns = "id", childColumns = "relationship_type"),
        primaryKeys = {"user_first_id", "user_second_id"})
public class UserRelationship {
    /******   For this to work, we need to enforce user_first_id < user_second_id or we may have
     ******   duplicate records.
     ******/
    @ColumnInfo(name = "user_first_id")
    private int userFirstId;

    @ColumnInfo(name = "user_second_id")
    private int userSecondId;

    @ColumnInfo(name = "relationship_type")
    private int relationshipType;

    public UserRelationship(int userFirstId, int userSecondId, int relationshipType) {
        this.userFirstId = userFirstId;
        this.userSecondId = userSecondId;
        this.relationshipType = relationshipType;
    }

    public int getUserFirstId() {
        return userFirstId;
    }

    public void setUserFirstId(int userFirstId) {
        this.userFirstId = userFirstId;
    }

    public int getUserSecondId() {
        return userSecondId;
    }

    public void setUserSecondId(int userSecondId) {
        this.userSecondId = userSecondId;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }
}
