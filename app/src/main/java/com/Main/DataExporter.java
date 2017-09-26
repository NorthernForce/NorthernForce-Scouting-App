package com.Main;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by AlexK on 1/18/2017.
 *
 * Can be used to export data to a csv file
 */
public class DataExporter {

    public DataExporter(){

    }

    public void exportData(String fileName){
        File sdDir = Environment.getExternalStorageDirectory();
        Log.v("MainActivity", "this is a test");
        if(sdDir.exists()){
            Log.v("MainActivity", "sdDir exists");
            Log.v("MainActivity", "sdDir absolute file path is " + sdDir.getAbsolutePath());
            if(sdDir.canWrite()){
                Log.v("MainActivity", "sdDir can write");
                
            } else{
                Log.v("Mainactivity", "sdDir can not write");
            }
        } else{
            Log.v("MainActivity", "sdDir does not exist");
        }
        File newPath = new File(sdDir.getAbsolutePath() + "/ScoutingApp");
        newPath.mkdirs();
        File writePath = new File(newPath, fileName);
//        MySQLiteHelper db = MainActivity.uiDatabaseInterface.getDatabase();
//         Cursor c = db.selectFromTable("*", "*");
        try {
            FileOutputStream fOS = new FileOutputStream(writePath);
            fOS.write("this is a test that better work".getBytes());


        }catch(IOException e){

        }
    }


}
