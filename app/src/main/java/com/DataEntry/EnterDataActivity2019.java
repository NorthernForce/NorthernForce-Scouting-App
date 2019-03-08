package com.DataEntry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.DataView.ViewDataActivity;
import com.DataView.ViewDataActivity2019;
import com.Main.MainActivity;
import com.Main.R;
import com.Main.UIDatabaseInterface2019;
import com.Settings.SettingsActivity;


/**
 * Created by AlexK on 12/22/2015.
 */
public class EnterDataActivity2019 extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private View dataEntryViews[];
    private boolean initialized = false;

    AppCompatEditText teamNumberText;
    AppCompatEditText matchNumberText;
    AppCompatButton submitButton;


    private TextWatcher teamAndMatchWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            // If there are values in both the team and match text, enable submit button
            // otherwise disable button
            String teamVal = teamNumberText.getText().toString();
            String matchVal = matchNumberText.getText().toString();
            boolean haveTeam = teamVal != null && (! teamVal.isEmpty());
            boolean haveMatch = matchVal != null && (! matchVal.isEmpty());
            submitButton.setEnabled(haveTeam && haveMatch);
        }
    };

    @Override
    /**
     * Gets the current database columns and sets them up to be displayed on the screen
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_entry_layout_2019);

        DataEntryRow2019 rows[] = UIDatabaseInterface2019.getDataEntryRows();

        dataEntryViews = new View[rows.length];

        Context c = this.getBaseContext();
        for(int i = 0; i < rows.length; i++){
            dataEntryViews[i] = rows[i].getView(c, this);
        }
        //create the list view to display the data
        this.createListView();
    }


    /**
     * Uses the list of dataEntry rows and appends each of them into the layout based on the format that they are in
     */
    private void createListView(){
        if (initialized) return;
        LinearLayout dynamicLayout = findViewById(R.id.dynamic_linear_layout);
        dynamicLayout.removeAllViewsInLayout();

        DataEntryRow2019 rows[] = UIDatabaseInterface2019.getDataEntryRows();
        Context c = this.getBaseContext();
        for(int i = 0; i < rows.length; i++){
            dataEntryViews[i] = rows[i].getView(c, this);
        }

        //goes through the rows and adds them to the linear layout
        int counter = 0;
        for(View view : dataEntryViews){
            dynamicLayout.addView(view, counter);
            counter++;
            Log.v("EnterDataActivity", "added a row to the dynamic layout");

        }
        //when the submit button is pressed 
        submitButton = findViewById(R.id.submitButton2019);
        if(submitButton == null){
            Log.e("EnterDataActivity", "submit button was null");
        }


        teamNumberText = (AppCompatEditText)((LinearLayout)dynamicLayout.getChildAt(0)).getChildAt(1);
        matchNumberText = (AppCompatEditText)((LinearLayout)dynamicLayout.getChildAt(1)).getChildAt(1);

        teamNumberText.addTextChangedListener(teamAndMatchWatch);
        matchNumberText.addTextChangedListener(teamAndMatchWatch);

        // disable the submit button until a team and match number are provided
        submitButton.setEnabled(false);

        final EnterDataActivity2019 e = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View newV = findViewById(R.id.dynamic_linear_layout);
                Log.v("EnterDataActivity", "Submit was pressed");
                AlertDialog.Builder builder = new AlertDialog.Builder(e);
                builder.setMessage("Are you sure you want to submit?")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                Log.v("missiles", "missiles fired");
                                UIDatabaseInterface2019.submitDataEntry(newV);

                                Snackbar mySnackbar = Snackbar.make(newV, "Submitted entry", Snackbar.LENGTH_SHORT);
                                mySnackbar.show();

                                e.initialized = false;
                                e.createListView();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                Log.v("missiles", "missiles canceled");
                            }
                        });
                builder.show();
            }
        });

        initialized = true;
        dynamicLayout.refreshDrawableState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_enterData){
            // this is us
        }

        else if (id == R.id.action_viewData){
            Intent i = new Intent(this, ViewDataActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_viewDataTable){
            Intent i = new Intent(this, ViewDataActivity2019.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    //when they select something from the spinner at the top
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedTable = (String) adapterView.getItemAtPosition(i);

        Log.v("EnterDataActivity", "The spinner selected the table " + selectedTable);

        UIDatabaseInterface2019.setCurrentDataEntryTable(selectedTable);

        UIDatabaseInterface2019.createDataEntryRows(UIDatabaseInterface2019.getTableList());

        this.createListView();
    }

    @Override
    //when they don't select something from the spinner at the top
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
