package com.example.cinemaguru;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
//Reference: https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android/
public class CinemaModal extends AndroidViewModel {
    private CinemaRepository repository;

    private LiveData<List<Cinema>> allCinemas;

    public CinemaModal(@NonNull Application application) {
        super(application);
        repository = new CinemaRepository(application);
        allCinemas = repository.getAllCinemas();
    }

    public void insert(Cinema model) {
        repository.insert(model);
    }
    // below line is to delete the data in our repository.
    public void delete(Cinema model) {
        repository.delete(model);
    }
} //End of reference
