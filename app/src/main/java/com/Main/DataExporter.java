package com.Main;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by AlexK on 1/18/2017.
 *
 * Can be used to export data to a csv file
 */
public class DataExporter extends AsyncTask<Void, Void, String>{

    private Socket client;
    private PrintWriter printwriter;
    private String[] lines;
    private final String START_INDICATOR = "///"; // "///"
    private final String END_INDICATOR = "\\\\\\"; // "\\\"


    protected String doInBackground(Void ... Params){
        lines = getCSVString(); //get the text message on the text field
        //textField.setText("");      //Reset the text field to blank

        try {

            client = new Socket("192.168.86.56", 4444);  //connect to server
            printwriter = new PrintWriter(client.getOutputStream(),true);
            printwriter.println(START_INDICATOR);
            printwriter.flush();
            for(String line : lines){
                printwriter.println(line);
            }
            printwriter.println(END_INDICATOR);

            printwriter.flush();
            printwriter.close();
            client.close();   //closing the connection

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String[] getCSVString() {


        MySQLiteHelper db = MainActivity.uiDatabaseInterface.getDatabase();
        Cursor c = db.selectFromTable("Performance", "*");
        String[] csv = new String[c.getCount() + 1]; //+1 for column names
        int counter = 0;

        String[] columnNames = c.getColumnNames();
        int numCol = c.getColumnCount();
        csv[counter] += columnNames[0];
        for (int i = 1; i < numCol; i++) {
            csv[counter] += "," + columnNames[i];
        }
        counter++;

        if (c.moveToFirst()) {
            do {
                csv[counter] = c.getString(0);
                for (int i = 1; i < numCol; i++) {
                    csv[counter] += "," + c.getString(i);
                }
                counter++;
                Log.v("DataExporter", "csv is " + csv);
            } while (c.moveToNext());
        }

        return csv;
    }


}
