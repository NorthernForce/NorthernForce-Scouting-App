package com.Main;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.Confg.ConfigEntry;
import com.Confg.ConfigParser;
import com.DataEntry.DataEntryRow2019;
import com.DataEntry.NumberDataEntryRow2019;
import com.DataEntry.SelectDataEntryRow2019;
import com.DataEntry.StringDataEntryRow2019;
import com.DataEntry.YorNDataEntryRow2019;

import org.apache.commons.csv.CSVRecord;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 4/18/15.
 */
public class UIDatabaseInterface2019 {

    private static String TAG = "UIDatabaseInterface";
    private static DatabaseTable teamTable;
    private static DatabaseTable matchTable;

    private static MySQLiteHelper database;

    private static DataEntryRow2019[] dataEntryRows;

    private static ArrayList<DatabaseTable> tables;

    private static String currentDataEntryTable;
    private static String currentDataViewTable;

    public UIDatabaseInterface2019(Context context){
        database = new MySQLiteHelper(context);

        database.onUpgrade(database.getWritableDatabase(), 0, 1);

        ConfigParser configParser = new ConfigParser();
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("configFile_2019.xml");
            Log.v("UIDI", is.toString());
            tables = configParser.parse(is);

            Log.v("UIDI", "the number of tables if " + tables.size());
            for(DatabaseTable table : tables){
                Log.v(TAG, "Found table " + table.getName() + " to make");

                if(!database.doesTableExists(table.getName())) {
                    database.createTable(table);
                }
            }
        } catch (XmlPullParserException e) {
            Log.e("UIDatabaseInterface", "XmlPullParserException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UIDatabaseInterface", "IOException");
        }

        listTables();

        listMatchesColumns();
        listPerformanceColumns();

        currentDataEntryTable = "Performance";
        currentDataViewTable = "Performance";

        try {
            createDataEntryRows(tables);
        }
        catch (IndexOutOfBoundsException iobe) {
            resetDatabase(context);
        }
        //this.populateDatabase();
    }

    public static void resetDatabase(Context context){
        database = new MySQLiteHelper(context);

        database.deleteDatabase();
        database.onUpgrade(database.getWritableDatabase(), 0, 1);

        ConfigParser configParser = new ConfigParser();
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("configFile_2019.xml");
            Log.v("UIDI", is.toString());
            tables = configParser.parse(is);

            Log.v("UIDI", "the number of tables if " + tables.size());
            for(DatabaseTable table : tables){
                Log.v(TAG, "Found table " + table.getName() + " to make");

                if(!database.doesTableExists(table.getName())) {
                    database.createTable(table);
                }
            }
        } catch (XmlPullParserException e) {
            Log.e("UIDatabaseInterface", "XmlPullParserException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UIDatabaseInterface", "IOException");
        }

        createDataEntryRows(tables);
    }
    private static void listTables(){
        for(String tableName : getTableNames()){
            Log.v(TAG, "one table is " + tableName);
        }
    }

    public static ArrayList<String> getTableNames(){
        ArrayList<String> tableList = new ArrayList<>();

        Cursor tables = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'");
        if(tables.moveToFirst()){
            do{
                tableList.add(tables.getString(0));
            }while(tables.moveToNext());
        }

        return tableList;
    }

    private static void listMatchesColumns(){
        Cursor c = database.selectFromTable("Matches", "*");
        String columns[] = c.getColumnNames();

        int columnCount = c.getColumnCount();
        Log.v(TAG, "Matches column count is " + columnCount);

        for(String columnName : columns){
            Log.v(TAG, "COLUMN IN MATCHES : " + columnName);
        }
    }

    private static void listPerformanceColumns(){
        Cursor c = database.selectFromTable("Performance", "*");
        String columns[] = c.getColumnNames();

        int columnCount = c.getColumnCount();
        Log.v(TAG, "Performance column count is " + columnCount);

        for(String columnName : columns){
            Log.v(TAG, "COLUMN IN Performance : " + columnName);
        }
    }


    /**
     * Goes through the data obtained for
     * @param tables
     */
    public static void createDataEntryRows(ArrayList<DatabaseTable> tables) {
        Cursor performance = database.selectFromTable(currentDataEntryTable, "*");
        int columnCount = performance.getColumnCount();

        for(String s : performance.getColumnNames()){
            Log.v("column names", s);
        }
        //minus one because of id column
        dataEntryRows = new DataEntryRow2019[columnCount - 1];

        ArrayList<ConfigEntry> columns = null;
        Log.v(TAG, "current data entry table " + currentDataEntryTable);
        Log.v(TAG, "column count is " + columnCount);
        for(DatabaseTable table : tables){
            if(table.getName().equals((currentDataEntryTable))){
                columns = table.getColumns();
            }
        }

        Log.v(TAG, "There are " + columns.size() + " columns");

        int counter = 0;
        for(ConfigEntry entry : columns){
            String type = entry.getType();
            String columnName = entry.getColName();
            String text = entry.getText();
            String selectOptions = entry.getSelectOptions();
            Boolean tableview = entry.getTableview();
            String tableHeader = entry.getTableHeader();
            String filters = entry.getFilters();

            DataEntryRow2019 row;

            switch (type) {
                case "String": row = new StringDataEntryRow2019(columnName, counter, text, tableview, tableHeader, filters);
                    break;
                case "Number": row = new NumberDataEntryRow2019(columnName, counter, text, tableview, tableHeader, filters);
                    break;
                case "YorN": row = new YorNDataEntryRow2019(columnName, counter, text, tableview, tableHeader, filters);
                    break;
                case "Select": row = new SelectDataEntryRow2019(columnName, counter, text, selectOptions, tableview, tableHeader, filters);
                    break;
                default: row = null;
                    break;
            }

            dataEntryRows[counter] = row;

            counter++;
        }
    }


    private static Set<String> getUniqueTeamMatches() {
        HashSet<String> set = new HashSet<>();
        ContentValues values = new ContentValues();

        Cursor c = database.rawQuery("Select distinct Team_Number, Match_Number from Performance");
        if (c.moveToFirst()) {
            do {
                String teamMatch = c.getString(0) + "-" + c.getString(1);
                set.add(teamMatch);
            }
            while (c.moveToNext());
        }
        return set;
    }

    public static void submitDataEntries(List<CSVRecord> records) {
        // Get the unique combination of teams and matches to avoid duplicates
        Set<String> uniqueTeamMatches = getUniqueTeamMatches();

        // Get the column names
        Cursor tableHeaders = database.rawQuery("SELECT * FROM Performance ORDER BY ROWID ASC LIMIT 1");
        String[] colNames = tableHeaders.getColumnNames();

        // For each csv record, create a database row if it doesn't already exist
        for (CSVRecord record : records) {
            String teamMatch = record.get(1) + "-" + record.get(2); // skip the id row
            if (uniqueTeamMatches.contains(teamMatch)) continue;

            ContentValues values = new ContentValues();
            for (int colNum = 1 ; colNum < colNames.length; colNum++) { // skip the id column
                values.put(colNames[colNum], record.get(colNum));
            }
            database.addValues("Performance", values);
        }

        updateTeamTable();
    }

    //takes the view of the screen when the submit button is pressed, puts that data in the database
    public static void submitDataEntry(View v) {
        ContentValues values = new ContentValues();

        for (DataEntryRow2019 row : dataEntryRows) {
            String value = row.getValue();
            Log.v(TAG, "submit value is " + value);
            values.put(row.getColumnName(), value); //this works because the display is in the same order as the database
        }
        database.addValues(currentDataEntryTable, values);

        updateTeamTable();
    }


    public static void populateDatabase(){
        for(int i = 0; i<10; i++){
            ContentValues values = new ContentValues();

            values.put("Team_Number", 1);

            values.put("Match_Number", "1");
            values.put("Score", "" + Math.ceil(Math.random()*100));
            values.put("Performance", "5");

            database.addValues(matchTable.getName(), values);

            Log.v(TAG, "populated " + matchTable.getName() + " and size is: " + database.countRowsInTable("Teams"));
        }
        updateTeamTable();
    }

    private static void updateTeamTable(){
        Cursor teams;
        teams = getTeamsNotInTeamTable();

        Log.v(TAG, "teams not in team table length is " + teams.getCount());
        if(teams.moveToFirst()){
            do{
                Log.v(TAG, "added team number " + teams.getString(0));
                ContentValues values = new ContentValues();
                values.put("Team_Number", teams.getString(0));
                database.addValues("Teams", values);
            }while(teams.moveToNext());
        }
        //averageScoreForTeams();
    }

    public static Cursor getAllTeams(){
        return database.selectFromTable("Matches", "Team_Number");
    }

    private static void averageScoreForTeams(){
        Cursor teams = database.selectFromTable("Teams", "Team_Number");

        int teamCount = teams.getCount();

        if(teamCount == 0){
            return;
        }

        if(teams.moveToFirst()){
            do{
                database.updateCell(teamTable.getName(),
                        "Average_Score", "" + getAverageScoreForTeams(teams.getInt(teams.getColumnIndex("Team_Number"))),
                        "Team_Number = " + teams.getInt(teams.getColumnIndex("Team_Number")));
            } while (teams.moveToNext());
        }
    }

    public static String dataBaseToString(){
        String returnString = "";

        ArrayList<DatabaseTable> tables = UIDatabaseInterface2019.tables;
        for(DatabaseTable table : tables){
            Cursor tableCursor = database.selectFromTable(table.getName(), "*");
            Log.v(TAG, "the number of columns in table " + table.getName() + " is " + tableCursor.getColumnCount());
            if(tableCursor.getColumnCount() == 0){
                break;
            }
            tableCursor.moveToFirst();

            returnString += "Table: " + table.getName() + "\n";
            for(String tableName: tableCursor.getColumnNames()){
                returnString += tableName + ", ";
            }
            returnString += "\n";
            while(!tableCursor.isLast()) {
                for (int i = 0; i < tableCursor.getColumnCount(); i++) {
                    returnString += tableCursor.getString(i) + ", ";
                }
                returnString += "\n";
                tableCursor.moveToNext();
            }
            returnString += "\n";
        }
        return returnString;
    }

    private static int getAverageScoreForTeams(int teamNumber){
        Cursor matches = database.selectFromTableWhere("Score", "Matches", "Team_Number = " + teamNumber);
        int count = matches.getCount();
        if(count == 0){
            return 0;
        }
        int average = 0;
        if(matches.moveToFirst()){
            do{
                average += matches.getInt(0);
            }while(matches.moveToNext());
        }
        average = average / count;
        return average;
    }

    private static Cursor getTeamsNotInTeamTable(){
        Cursor teams = database.selectFromTableExcept("Team_Number", "Performance", "SELECT Team_Number FROM Teams");
        Log.v(TAG, "teams not in team table: " + teams.getCount());
        return teams;
    }

    public static DataEntryRow2019[] getDataEntryRows(){
        return dataEntryRows;
    }

    public static MySQLiteHelper getDatabase(){
        return database;
    }

    public static ArrayList<DatabaseTable> getTableList(){
        return tables;
    }

    public static String getCurrentDataEntryTable(){
        return currentDataEntryTable;
    }

    public static void setCurrentDataEntryTable(String table){
        currentDataEntryTable = table;
    }

    public static String getCurrentDataViewTable(){
        return currentDataViewTable;
    }

    public static void setCurrentDataViewTable(String table){
        currentDataViewTable = table;
    }

}
