package com.example.cinemaguru;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Cinema> cinemas; // List to hold Cinema objects
    private List<String> cinemaNames; // List to hold cinema names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the list, adapter, and ListView
        cinemas = new ArrayList<>();
        cinemaNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cinemaNames);
        listView = findViewById(R.id.listViewLocations);
        listView.setAdapter(adapter);

        // Set up the delete and rename options(Wasn't able to have it running properly without the incorporation of this), was recommended by android studio. The text controls both methods etc it allows user to choose which method to use
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int listitem, long l) {

                String cinemaNameToModify = cinemaNames.get(listitem);

                // Dialog to give user an option of what to do
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Options for " + cinemaNameToModify)
                        .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Show user the newly named cinema
                                showRenameDialog(cinemaNameToModify, listitem);
                            }
                        })
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Show user the newly deleted cinema
                                showRemoveDialog(cinemaNameToModify, listitem);
                            }
                        })
                        .create().show();

                return false;
            }
        });

        // Set up for the maps.java file
        Button findButton = findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the MapsActivity when the "Find" button is clicked
                Intent intent = new Intent(MainActivity.this, maps.class);
                startActivity(intent);
            }
        });

        // Set up for the google_maps.java file
        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, google_maps.class);
                startActivity(intent);
            }
        });

        // There to ensure once a change is made, it is seen across all platforms.
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        database.cinemaDao().getAllCinemas().observe(this, new Observer<List<Cinema>>() {
            @Override
            public void onChanged(List<Cinema> cinemas) {
                updateUI(cinemas);
            }
        });

        // Receive data from the maps activity
        Intent intent = getIntent();
        if (intent.hasExtra("cinemaName")) {
            String cinemaName = intent.getStringExtra("cinemaName");

        }
    }

    private void updateUI(List<Cinema> cinemas) {
        cinemaNames.clear();
        for (Cinema cinema : cinemas) {
            cinemaNames.add(cinema.getName());
        }
        adapter.notifyDataSetChanged();
    }

    // Function/Method to help update the name of the item in list and database. // Reference: https://stackoverflow.com/questions/21153046/renaming-an-item-in-a-listview (For the update)
    private void showRenameDialog(String currentName, int listitem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename Cinema");
        final EditText input = new EditText(this);
        input.setText(currentName);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = input.getText().toString();
                if (!newName.isEmpty()) {
                    // Updates the list
                    cinemaNames.set(listitem, newName);
                    adapter.notifyDataSetChanged();

                    // Updates the database
                    new RenameCinemaTask(MainActivity.this).execute(currentName, newName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
    // End of Reference(Had to manipulate it of course to meet required features

    // Method/Function to delete the item from database and list //Reference:https://www.youtube.com/watch?v=KO8tSyTmV24
    private void showRemoveDialog(String cinemaNameToRemove, int listitem) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Do you want to remove " + cinemaNameToRemove + " from list?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Remove from the list
                        cinemaNames.remove(listitem);
                        adapter.notifyDataSetChanged();

                        // Remove from the Room database
                        new DeleteCinemaTask(MainActivity.this).execute(cinemaNameToRemove);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
    // End of Reference

    // AsyncTask to delete Cinema object // Reference for these Async Procedures:https://www.geeksforgeeks.org/how-to-add-custom-marker-to-google-maps-in-android/
    private static class DeleteCinemaTask extends AsyncTask<String, Void, Void> {
        private CinemaDao cinemaDao;

        private DeleteCinemaTask(Context context) {
            AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
            cinemaDao = db.cinemaDao();
        }

        @Override
        protected Void doInBackground(String... cinemaNames) {
            // Delete the cinema in the background
            if (cinemaNames != null && cinemaNames.length > 0) {
                // To find the cinema by name and remove it from the database
                Cinema cinemaToDelete = cinemaDao.getCinemaByName(cinemaNames[0]);

                // Deletes the cinema from the database the user created
                cinemaDao.delete(cinemaToDelete);
            }
            return null;
        }
    }

    // AsyncTask to help rename the cinema the user chooses to Rename
    private static class RenameCinemaTask extends AsyncTask<String, Void, Void> {
        private CinemaDao cinemaDao;

        private RenameCinemaTask(Context context) {
            AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
            cinemaDao = db.cinemaDao();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params != null && params.length == 2) {
                String oldName = params[0];
                String newName = params[1];

                // Updates the cinema name in the list and the database
                cinemaDao.renameCinema(oldName, newName);
            }
            return null;
        }
    }
} //End of reference
