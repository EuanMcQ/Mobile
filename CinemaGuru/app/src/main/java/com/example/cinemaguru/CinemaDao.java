package com.example.cinemaguru;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//Reference:https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android/
@Dao
public interface CinemaDao {
    @Insert
    long insert(Cinema cinema);

    @Delete
    void delete(Cinema cinema);

    @Query("SELECT * FROM cinema")
    LiveData<List<Cinema>> getAllCinemas();

    @Query("SELECT * FROM cinema WHERE name = :name")
    Cinema getCinemaByName(String name);

    @Query("UPDATE cinema SET name = :newName WHERE name = :oldName")
    void renameCinema(String oldName, String newName);
} //End of reference
