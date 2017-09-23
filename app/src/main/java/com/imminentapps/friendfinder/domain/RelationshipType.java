package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mburke on 9/21/17.
 */
@Entity(tableName = "relationship_type")
public class RelationshipType {
    @PrimaryKey
    private int id;
    private String type;

    public RelationshipType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
