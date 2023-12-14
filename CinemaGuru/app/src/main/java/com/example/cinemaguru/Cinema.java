package com.example.cinemaguru;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
//Reference: https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android/
@Entity
public class Cinema {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;
    private String location;

    public Cinema(String name, String location) {
        this.name = name;
        this.location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String name) {
        this.location = location;
    }
} // End of reference
