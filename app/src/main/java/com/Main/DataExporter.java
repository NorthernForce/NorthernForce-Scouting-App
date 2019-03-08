package com.Main;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

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

    public static String exportNow(boolean includeHeader) {
        String[] exportLines = getCSVString();
        StringBuilder buffer = new StringBuilder();
        boolean isHeader = true;
        for (String line : exportLines) {
            if ((isHeader && includeHeader) || (! isHeader)) {
                buffer.append(line).append("\r\n");
            }
            isHeader = false;
        }
        Log.v("DataExporter", "csv is " + buffer.toString());
        return buffer.toString();
    }

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

    private static String[] getCSVString() {


        MySQLiteHelper db = UIDatabaseInterface2019.getDatabase();
        Cursor c = db.selectFromTable("Performance", "*");
        String[] csv = new String[c.getCount() + 1]; //+1 for column names
        int counter = 0;

        String[] columnNames = c.getColumnNames();
        int numCol = c.getColumnCount();
        StringBuilder lineBuffer = new StringBuilder("\"");
        lineBuffer.append(columnNames[0]);
        for (int i = 1; i < numCol; i++) {
            lineBuffer.append("\",\"").append(columnNames[i]);
        }
        lineBuffer.append("\"");
        csv[counter++] = lineBuffer.toString();

        if (c.moveToFirst()) {
            do {
                lineBuffer = new StringBuilder("\"");
                lineBuffer.append(c.getString(0));
                for (int i = 1; i < numCol; i++) {
                    lineBuffer.append("\",\"").append(c.getString(i));
                }
                lineBuffer.append("\"");
                csv[counter++] = lineBuffer.toString();
                Log.v("DataExporter", "csv is " + csv.toString());
            } while (c.moveToNext());
        }

        return csv;
    }


}
