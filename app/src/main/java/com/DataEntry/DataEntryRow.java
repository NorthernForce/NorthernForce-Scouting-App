package com.DataEntry;

import android.content.Context;
import android.view.View;

/**
 * Created by alex on 4/20/15.
 */
public abstract class DataEntryRow {

    protected String type;
    protected String columnName;
    protected String text;

    protected DataEntryRow(String type, String columnName, String text){
        this.type = type;
        this.columnName = columnName;
        this.text = text;
    }

    public abstract View getView(Context c);

    public abstract String getValue();

    public String getType(){
        return this.type;
    }

    public String getColumnName(){
        return this.columnName;
    }

    public String getText() {
        return text;
    }
}
