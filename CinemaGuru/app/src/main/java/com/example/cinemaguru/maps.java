package com.example.cinemaguru;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class maps extends FragmentActivity implements LocationListener {

    private EditText cinemaNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps); //Name of the xml file.

        cinemaNameEditText = findViewById(R.id.editTextLocation);

        // Add button to add to the database and list
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting the name for the text bar(UI)
                String cinemaName = cinemaNameEditText.getText().toString();

                // Save the cinema name to the database in the background(Thread)
                new InsertCinemaTask(maps.this).execute(new Cinema(cinemaName, ""));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // I couldn't get the code working without this line here, as I was changing features consistently,hence the map button was created as I couldn't get the maps working properly on this.
    }

    // AsyncTask to insert cinema in the background, smoother process
    private static class InsertCinemaTask extends AsyncTask<Cinema, Void, Long> {
        private final Context context;

        InsertCinemaTask(Context context) {
            this.context = context;
        }

        @Override //Reference: https://www.geeksforgeeks.org/how-to-add-custom-marker-to-google-maps-in-android/
        protected Long doInBackground(Cinema... cinemas) {
            AppDatabase database = AppDatabase.getInstance(context);
            return database.cinemaDao().insert(cinemas[0]);
        }

        @Override
        protected void onPostExecute(Long rowId) {
            super.onPostExecute(rowId);
            if (rowId != -1) {
                // If the insertion is successful, inform the user
                Toast.makeText(context, "Cinema added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add cinema", Toast.LENGTH_SHORT).show();
            }
        }
    }// End of reference
}
