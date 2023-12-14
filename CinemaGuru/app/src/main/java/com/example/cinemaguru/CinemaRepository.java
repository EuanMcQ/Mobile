package com.example.cinemaguru;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
//Reference:https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android/
public class CinemaRepository {
    private CinemaDao cinemaDao;

    private LiveData<List<Cinema>> allCinemas;

    // creating a constructor for our variables
    public CinemaRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        cinemaDao = db.cinemaDao();
        allCinemas = cinemaDao.getAllCinemas();
    }

    public LiveData<List<Cinema>> getAllCinemas() {
        return allCinemas;
    }

    public void insert(Cinema model) {
        new InsertCourseAsyncTask(cinemaDao).execute(model);
    }

    public void delete(Cinema model) {
        new DeleteCourseAsyncTask(cinemaDao).execute(model);
    }

    public long addCinema(String cinemaName, String cinemaLocation) {
        Cinema cinema = new Cinema(cinemaName, cinemaLocation);

        // Insert the cinema into the database
        return cinemaDao.insert(cinema);
    }

    // Async operations below.
    private static class InsertCourseAsyncTask extends AsyncTask<Cinema, Void, Void> {
        private CinemaDao dao;

        private InsertCourseAsyncTask(CinemaDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Cinema... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Cinema, Void, Void> {
        private CinemaDao dao;

        private DeleteCourseAsyncTask(CinemaDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Cinema... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }
} //End of reference.
