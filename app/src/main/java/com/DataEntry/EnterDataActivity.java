package com.DataEntry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.Main.MainActivity;
import com.Main.R;
import com.Main.UIDatabaseInterface;


/**
 * Created by AlexK on 12/22/2015.
 */
public class EnterDataActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private View dataEntryViews[];

    @Override
    /**
     * Gets the current database columsn and sets them up to be displayed on the screen
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_entry_layout);

        DataEntryRow rows[] = UIDatabaseInterface.getDataEntryRows();

        dataEntryViews = new View[rows.length];

        Context c = this.getBaseContext();
        for(int i = 0; i < rows.length; i++){
            dataEntryViews[i] = rows[i].getView(c, this);
        }
        //create the list view to display the data
        this.createListView();
    }

    //create the list view

    /**
     * Uses the list of dataEntry rows and appends each of them into the layout based on the format that they are in
     */
    private void createListView(){
        LinearLayout dataEntryLinearLayout = (LinearLayout)findViewById(R.id.dataEntryLinearLayout);

        dataEntryLinearLayout.removeViews(0, dataEntryLinearLayout.getChildCount() - 1); //leave submit button
        DataEntryRow rows[] = UIDatabaseInterface.getDataEntryRows();
        Context c = this.getBaseContext();
        for(int i = 0; i < rows.length; i++){
            dataEntryViews[i] = rows[i].getView(c, this);
        }

        //goes through the rows and adds them to the linear layout
        int counter = 0;
        for(View view : dataEntryViews){
            dataEntryLinearLayout.addView(view, counter);
            counter++;
            Log.v("EnterDataActivity", "added a row to the linear layout");

        }
        //when the submit button is pressed 
        Button submitButton = (Button) findViewById(R.id.submitButton);
        if(submitButton == null){
            Log.e("EnterDataActivity", "submit button was null");
        }
        final EnterDataActivity e = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View newV = v;
                Log.v("EnterDataActivity", "Submit was pressed");
                AlertDialog.Builder builder = new AlertDialog.Builder(e);
                builder.setMessage("Are you sure you want to submit?")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                Log.v("missiles", "missiles fired");
                                UIDatabaseInterface.submitDataEntry(newV);
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        if (id == R.id.action_enterData){
            Intent i = new Intent(this, MainActivity.class);

            startActivity(i);
        }

        if (id == R.id.action_viewData){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    //when they select something from the spinner at the top
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedTable = (String) adapterView.getItemAtPosition(i);

        Log.v("EnterDataActivity", "The spinner selected the table " + selectedTable);

        UIDatabaseInterface.setCurrentDataEntryTable(selectedTable);

        UIDatabaseInterface.createDataEntryRows(UIDatabaseInterface.getTableList());

        this.createListView();
    }

    @Override
    //when they don't select something from the spinner at the top
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
