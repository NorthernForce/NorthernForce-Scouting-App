package com.DataView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.DataEntry.DataEntryRow2019;
import com.Main.MainActivity;
import com.Main.MySQLiteHelper;
import com.Main.R;
import com.Main.UIDatabaseInterface2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import edu.falgor.frc.dynamicapp.StringArrayComparator;

/**
 * Created by alex on 3/9/15.
 */
public class ViewDataActivity2019v1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MySQLiteHelper mySQLiteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.data_view_2019);

        mySQLiteHelper = UIDatabaseInterface2019.getDatabase();

        ArrayList<String> tables = UIDatabaseInterface2019.getTableNames();

        tables.remove("android_metadata");

        this.createSortableTableView();
    }

    private void createSortableTableView() {

        SortableTableView<String[]> tableView = findViewById(R.id.dataViewGridView2019);


        DataEntryRow2019[] entries = UIDatabaseInterface2019.getDataEntryRows();
        if ((entries == null) || (entries.length == 0)) return;

        // Create a list of only the table view data entry rows
        List<DataEntryRow2019> visibleEntries = new ArrayList<>(entries.length);
        List<String> headersList = new ArrayList<>(entries.length);
        for (DataEntryRow2019 entry : entries) {
            if (entry.isTableview()) {
                visibleEntries.add(entry);
                headersList.add(entry.getTableHeader());
            }
        }
        int columnCount = visibleEntries.size();
        tableView.setColumnCount(columnCount);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headersList.toArray(new String[columnCount])));
        tableView.setHeaderBackgroundColor(getResources().getColor(R.color.CornflowerBlue));


        Cursor cursor = mySQLiteHelper.selectFromTable("Performance", "*");
        int numRows = cursor.getCount();
        cursor.moveToFirst();

        // Build the double-array of data.
        // Only show the ones that are table visible,
        String[][] data = new String[numRows][columnCount];
        for (int rowNum = 0; rowNum < numRows; rowNum++) {
            for (int colNum = 0; colNum < columnCount; colNum++) {
                // Don't get the cursor column directly, look it up from the map
                DataEntryRow2019 visibleEntry = visibleEntries.get(colNum);
                data[rowNum][colNum] = cursor.getString(visibleEntry.getColumnNumber());

                Comparator<?> c;
                switch (visibleEntry.getType()) {
                    case "Number" :
                        c = new NumberArrayComparator(colNum);
                        break;
                    default :
                        c = new StringArrayComparator(colNum);
                }
                tableView.setColumnComparator(colNum, new StringArrayComparator(colNum));
            }
            cursor.moveToNext();
        }

        //tableView.setColumnComparator(0, new StringArrayComparator(0));
        tableView.setDataAdapter(new SimpleTableDataAdapter(super.getApplicationContext(), data));

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        UIDatabaseInterface2019.setCurrentDataViewTable("Performance");

        this.createSortableTableView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
