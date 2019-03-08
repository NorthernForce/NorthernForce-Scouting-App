package com.Settings;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.Main.R;
import com.Main.UIDatabaseInterface2019;

/**
 * Created by Alex Kinley on 2/25/2018.
 */

public class SettingsActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        final SettingsActivity a = this;
        Button resetDatabase = (Button) findViewById(R.id.settings_resetdatabase);
        resetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(a);
                builder.setMessage("Are you sure you want to do this? This will delete all current data")
                        .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertDialog.Builder builderTwo = new AlertDialog.Builder(a);
                                builderTwo.setMessage("Are you REALLY sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                UIDatabaseInterface2019.resetDatabase(getBaseContext());
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                builderTwo.show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            }
        });
    }
}
