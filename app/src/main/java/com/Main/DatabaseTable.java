package com.Main;

import com.Confg.ConfigEntry;

import java.util.ArrayList;

/**
 * Created by alex on 4/19/15.
 * Class encapsulating database tables, contains the table name and a list of column names
 */
public class DatabaseTable {

    private String name;
    private ArrayList<ConfigEntry> columms;

    public DatabaseTable(){

    }

    public DatabaseTable(String name, ArrayList<ConfigEntry> columns){
        this.name = name;
        this.columms = columns;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<ConfigEntry> getColumns(){
        return this.columms;
    }

    public void setColumns(ArrayList<ConfigEntry> columns){
        this.columms = columns;
    }
}
