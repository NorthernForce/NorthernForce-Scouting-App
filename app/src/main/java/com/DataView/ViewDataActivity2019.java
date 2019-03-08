package com.DataView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.DataEntry.DataEntryRow2019;
import com.DataEntry.EnterDataActivity2019;
import com.Main.MainActivity;
import com.Main.MySQLiteHelper;
import com.Main.R;
import com.Main.UIDatabaseInterface2019;
import com.Settings.SettingsActivity;
import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 3/9/15.
 */
public class ViewDataActivity2019 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MySQLiteHelper mySQLiteHelper;
    private TableView mTableView;
    private TableAdapter mTableAdapter;
    private SwitchCompat averageTeamSwitch;
    private List<SwitchCompat> filterSwitches = new LinkedList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutUI();
    }

    private void layoutUI() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.data_view_2019);

        mTableView = findViewById(R.id.dataViewGridView2019);
        averageTeamSwitch = findViewById(R.id.averageTeamSwitch);
        filterSwitches.add(averageTeamSwitch);
        //averageTeamSwitch.setOnCheckedChangeListener(filterSwitchListener);


        mySQLiteHelper = UIDatabaseInterface2019.getDatabase();

        /* this part should be dynamic*/
        //createFilters();

        // but make static for now
        SwitchCompat sandstormFilter = findViewById(R.id.sandstormSwitch);
        filterSwitches.add(sandstormFilter);
        SwitchCompat teleOpFilter = findViewById(R.id.teleopSwitch);
        filterSwitches.add(teleOpFilter);

        for (SwitchCompat filter : filterSwitches) {
            filter.setOnCheckedChangeListener(filterSwitchListener);
        }

        averageTeamSwitch.setOnCheckedChangeListener(filterSwitchListener);

        createSortableTableView(mTableView);
    }

    /**
     * Create the filter toggle switches across the top for filtering data
     */
    private void createFilters() {

        /* the code below should dynamically create all the filters.
        But when the orientation changes from portrait to landscape, something
        is happening to the dynamically created switches, and two "Tele-Op"
        // switches show instead of a sand storm and then a tele-op.

        Unfortunately, for now, don't do this dynamically and just get the two switches
        from the layout

        System.out.println("laying out filters");
        LinearLayout filterLayout = findViewById(R.id.filterLayout);

        // Need to go through all the entries and find the filters
        DataEntryRow2019[] entries = UIDatabaseInterface2019.getDataEntryRows();
        Set<String> distinctFilters = new TreeSet<>();
        for (DataEntryRow2019 entry : entries) {
            for (String filterName : entry.getFilters()) {
                distinctFilters.add(filterName);
            }
        }

        for (String filterName : distinctFilters) {
            View switchView = new DataFilter(filterName).getView(super.getApplicationContext(), this);
            filterLayout.addView(switchView);
            SwitchCompat filterSwitch = (SwitchCompat)(((LinearLayout )switchView).getChildAt(0));
            filterSwitches.add(filterSwitch);
            filterSwitch.setOnCheckedChangeListener(filterSwitchListener);
        }

        System.out.println("refresh draw state");
        filterLayout.refreshDrawableState();
        System.out.println("done redraw");

         */
    }

    /**
     * Redraws the table by looking at all the filter states
     */
    private void redrawWithFilters() {

    }

    private void createSortableTableView(TableView tableView) {

        // Create TableView Adapter
        DataEntryRow2019[] entries = filterColumns();
        mTableAdapter = new TableAdapter(super.getApplicationContext(), entries);
        tableView.setAdapter(mTableAdapter);

        Cursor cursor = mySQLiteHelper.selectFromTable("Performance", "*");
        List<List<Object>> data = filterData(cursor, entries);

        mTableAdapter.setData(data);
        tableView.buildLayer();
    }


    /**
     * Filter out the data entry rows based on the selected filters
     */
    public DataEntryRow2019[] filterColumns() {

        Set<String> appliedFilters = new HashSet<>();
        // go through the filters and find which ones are turned on
        for (SwitchCompat filterSwitch : filterSwitches) {
            if (filterSwitch.isChecked()) appliedFilters.add(filterSwitch.getText().toString());
        }

        DataEntryRow2019[] allEntries = UIDatabaseInterface2019.getDataEntryRows();
        List<DataEntryRow2019> filteredEntries = new ArrayList<>(allEntries.length);
        for (DataEntryRow2019 entryRow : allEntries) {
            Set<String> entryFilters = entryRow.getFilters();
            if (entryFilters.isEmpty()) filteredEntries.add(entryRow); // If there are no filters, always use it
            else {
                // otherwise, see if one of the applied filters matches the filters specified
                for (String appliedFilter : appliedFilters) {
                    if (entryFilters.contains(appliedFilter)) {
                        filteredEntries.add(entryRow);
                        break; // already found a match, no need to continue
                    }
                }// end loop on the applied filters
            } // end else of whether there are any filters
        } // end loop over all entries.

        return filteredEntries.toArray(new DataEntryRow2019[filteredEntries.size()]);
    }

    /**
     * Returns all the data that should be displayed, accounting for averaging data.
     */
    public List<List<Object>> filterData(Cursor cursor, DataEntryRow2019[] filteredEntries) {

        // Create a map of only the table view data entry rows
        Map<Integer, DataEntryRow2019> visibleEntryRowMap = new HashMap<>();
        int columnNum = 0;
        for (DataEntryRow2019 entry : filteredEntries) {
            if (entry.isTableview()) {
                visibleEntryRowMap.put(columnNum++, entry);
            }
        }

        // Put all the data in the cursor into lists
        // Keep track of unique team ids
        int numRows = cursor.getCount();
        int columnCount = visibleEntryRowMap.size();
        List<List<Object>> lists = new ArrayList<>(numRows);

        Set<String> teamIds = new HashSet<>();

        cursor.moveToFirst();
        for (int rowNum = 0; rowNum < numRows; rowNum++) {
            List<Object> list = new ArrayList<>(columnCount);
            for (int colNum = 0; colNum < columnCount; colNum++) {
                DataEntryRow2019 visibleEntry = visibleEntryRowMap.get(colNum);
                String value = cursor.getString(visibleEntry.getColumnNumber() + 1); // add 1 to skip over id column
                Object valObj = value;
                if (visibleEntry.getType().equals("Number")) {
                    if ((value == null) || (value.isEmpty())) valObj = 0;
                    else valObj = Integer.valueOf(value);
                }

                if (visibleEntry.getText().equals("Team Number")) teamIds.add(value);
                list.add(valObj);
            }

            // Add
            lists.add(list);
            cursor.moveToNext();
        }

        // if we are not averaging, we are done
        if (! averageTeamSwitch.isChecked()) return lists;

        // Otherwise, compute averages for each team
        Map<String, List<Object>> teamMap = new HashMap<>();
        Map<String, Integer> teamRecordCountMap = new HashMap<>(); // keeps track of how many rows per team

        for (String teamId : teamIds) {
            for (List<Object> row : lists) {
                // for each row, check to see if it's for this team.
                if (! row.get(0).toString().equals(teamId)) continue;

                Integer teamRecordCount = teamRecordCountMap.get(teamId);
                if (teamRecordCount == null) teamRecordCount = 1;
                else teamRecordCount = teamRecordCount + 1;
                teamRecordCountMap.put(teamId, teamRecordCount);


                // If it's the first entry for this team, create a new list
                List<Object> teamTotals = teamMap.get(teamId);
                if (teamTotals == null) {
                    teamTotals = new ArrayList<>(row.size());
                    teamTotals.add(teamId);
                    teamMap.put(teamId, teamTotals);

                    for (int i = 1; i < row.size(); i++) {
                        teamTotals.add(0);
                    }
                }

                // Add the current row to the existing totals
                // Handle empty Strings for numbers and yes/no
                for (int i = 1; i < row.size(); i++) {
                    Integer existingVal =(Integer)teamTotals.get(i);
                    if (existingVal == null) existingVal = 0;
                    Object currentVal = row.get(i);
                    int currentValInt;
                    if (currentVal == null || currentVal.toString().isEmpty()) currentValInt = 0;
                    else if (currentVal.toString().equals("yes")) currentValInt = 100;
                    else if (currentVal.toString().equals("no")) currentValInt = 0;
                    else currentValInt = Integer.parseInt(currentVal.toString());
                    int newTotal = existingVal + currentValInt;
                    teamTotals.set(i, newTotal);
                }
            }
        } // end loop through all the teams

        // The map should contain lists of all totals. Compute averages now
        List<List<Object>> averageLists = new ArrayList<>(teamMap.size());

        for (Map.Entry<String, List<Object>> teamEntry : teamMap.entrySet()) {
            String teamId = teamEntry.getKey();
            int numRecords = teamRecordCountMap.get(teamId);
            List<Object> teamTotals = teamEntry.getValue();

            // If there was only 1 record, we still need to average
            // otherwise we will have Integers in some cells and Doubles in others,
            // which crashes the sort code
            /*
            if (numRecords == 1) {
                averageLists.add(teamTotals);
                continue;
            }
            */

            List<Object> teamAverages = new ArrayList<>(teamTotals.size());
            teamAverages.add(teamId);

            for (int i = 1; i < teamTotals.size(); i++) {
                Integer existingVal = (Integer) teamTotals.get(i);
                double avgVal = ((double)existingVal) / numRecords;
                teamAverages.add(avgVal);
            }
            averageLists.add(teamAverages);
        }
        return averageLists;
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
            Intent i = new Intent(this, EnterDataActivity2019.class);
            startActivity(i);
        }

        else if (id == R.id.action_viewData){
            Intent i = new Intent(this, ViewDataActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_viewDataTable) {
            // this is us
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    /**
     * Listener for the filter toggles.  Find what data should be hidden/shown/averaged
     * and redo the table
     */
    private  CompoundButton.OnCheckedChangeListener filterSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            createSortableTableView(mTableView);
        }
    };
}
