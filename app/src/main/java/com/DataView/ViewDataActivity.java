package com.DataView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.DataEntry.EnterDataActivity2019;
import com.Main.MainActivity;
import com.Main.MySQLiteHelper;
import com.Main.R;
import com.Main.UIDatabaseInterface;
import com.Main.UIDatabaseInterface2019;
import com.Settings.SettingsActivity;

import java.util.ArrayList;

/**
 * Created by alex on 3/9/15.
 */
public class ViewDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MySQLiteHelper mySQLiteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_view);

        mySQLiteHelper = UIDatabaseInterface2019.getDatabase();

        ArrayList<String> tables = UIDatabaseInterface2019.getTableNames();

        tables.remove("android_metadata");

        Spinner tableSpinner = (Spinner) (findViewById(R.id.dataViewTableSpinner));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, tables);
        tableSpinner.setAdapter(spinnerAdapter);
        tableSpinner.setOnItemSelectedListener(this);

        this.createGridView();
    }


    private void createGridView(){
        GridView gridView = (GridView) (findViewById(R.id.dataViewGridView));

        final ViewDataAdapter2019 viewDataAdapter = new ViewDataAdapter2019(mySQLiteHelper, this);

        gridView.setAdapter(viewDataAdapter);
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_enterData){
            Intent i = new Intent(this, EnterDataActivity2019.class);
            startActivity(i);
        }

        else if (id == R.id.action_viewData){
            // this is us
        }

        else if (id == R.id.action_viewDataTable) {
            Intent i = new Intent(this, ViewDataActivity2019.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedTable = (String) adapterView.getItemAtPosition(i);

        Log.v("EnterDataActivity", "The spinner selected the table " + selectedTable);

        UIDatabaseInterface.setCurrentDataViewTable(selectedTable);
        this.createGridView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
