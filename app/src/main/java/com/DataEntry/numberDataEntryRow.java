package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.Main.R;

public class numberDataEntryRow extends DataEntryRow {
    private TextView textView;
    private EditText editText;

    public numberDataEntryRow(String columnName, String text){
        super("Number", columnName, text);
    }

    /**
     *
     * @param c
     * @param a the activity used to get the layout inflater
     * @return A created view to be added to the data entry screen
     */
    public View getView(Context c, Activity a){
        Log.v("numberDataEntryRow", "getView was called");

        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_number, null);

        textView = (TextView) rootView.findViewById(R.id.de_number_TextView);
        textView.setText(this.text);

        editText = (EditText) rootView.findViewById(R.id.de_number_EditText);
        //editText.setHint(columnName);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        return rootView;
    }

    public String getValue(){
        String value = editText.getText().toString();
        Log.v("numberDataEntryRow", "in getValue() returning " + value);
        return value;
    }
}
